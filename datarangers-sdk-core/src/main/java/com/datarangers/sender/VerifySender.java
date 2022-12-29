package com.datarangers.sender;

import com.datarangers.config.RangersJSONConfig;
import com.datarangers.config.VerifyConfig;
import com.datarangers.message.Message;
import com.datarangers.message.MessageEnv;
import com.datarangers.message.MessageType;
import com.datarangers.message.saas.DefaultSaasServerAppMessage;
import com.datarangers.sender.saasnative.SaasNativeMessageSender;
import com.datarangers.util.HttpUtils;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @Author zhangpeng.spin
 * @Date 2022/11/28
 */
public class VerifySender {
    public static final Logger logger = LoggerFactory.getLogger(VerifySender.class);
    private static final String QUERY_USER_ID = "user_id";
    private static final String QUERY_VALID_TIME = "valid_time";

    private VerifyConfig verify;

    private long validTime;
    private String verifyUserId;
    private boolean isValid;

    public VerifySender(VerifyConfig verify) {
        this.verify = verify;
        parseUrl();
    }

    public void send(Message message) {
        // 不需要进行校验
        if (!isValid) {
            return;
        }
        // 是否过期，判断当前时间是否超过
        if (System.currentTimeMillis() >= validTime) {
            return;
        }
        // 是否是调试的用户
        if (!verifyUserId.equals(message.getAppMessage().getUserUniqueId())) {
            return;
        }

        doSend(message);
    }

    private void doSend(Message message) {
        // 只支持saas
        MessageEnv messageEnv = message.getMessageEnv();
        switch (messageEnv) {
            case SAAS:
                verifySaas(message);
                break;
            case SAAS_NATIVE:
                verifySaasNative(message);
                break;
            case PRIVATIZATION:
                verifyPrivatization(message);
                break;
            default:
                throw new IllegalArgumentException("Verify not support message env: " + messageEnv);
        }
    }

    private void verifySaas(Message message) {
        // 只支持事件
        MessageType messageType = message.getMessageType();
        if (MessageType.EVENT != messageType) {
            System.out.println("Verify Not support messageType: " + messageType);
            logger.warn("Verify Not support messageType: " + messageType);
            return;
        }
        // 发送
        Object sendMessage = new DefaultSaasServerAppMessage(message);
        Map<String, String> headers = new HashMap<>();
        request("POST", verify.getUrl(), RangersJSONConfig.getInstance().toJson(sendMessage), headers);
    }

    private void verifySaasNative(Message message) {
        // 云原生使用event/json,or event/list的格式进行上报
        Object sendMessage = new DefaultSaasServerAppMessage(message);
        Map<String, String> headers = new HashMap<>();
        request("POST", verify.getUrl(), RangersJSONConfig.getInstance().toJson(sendMessage), headers);
    }

    private void verifyPrivatization(Message message) {
        // 私有化使用event/json,or event/list的格式进行上报
        Object sendMessage = new DefaultSaasServerAppMessage(message);;
        Map<String, String> headers = new HashMap<>();
        request("POST", verify.getUrl(), RangersJSONConfig.getInstance().toJson(sendMessage), headers);
    }

    private void parseUrl() {
        if (verify == null || verify.getUrl() == null || verify.getUrl().length() == 0) {
            return;
        }
        try {
            Map<String, String> params = splitQuery(new URL(verify.getUrl()));
            String validTimeStr = params.get(QUERY_VALID_TIME);
            if (validTimeStr == null || validTimeStr.length() == 0) {
                logger.warn("{} is not exist", QUERY_VALID_TIME);
                return;
            }
            validTime = Long.valueOf(new String(Base64.getDecoder().decode(validTimeStr), StandardCharsets.UTF_8)) * 1000;
            verifyUserId = params.get(QUERY_USER_ID);
            if (verifyUserId == null || verifyUserId.length() == 0) {
                logger.warn("{} is not exist", QUERY_USER_ID);
                return;
            }
            // 是否过期，判断当前时间是否超过
            if (System.currentTimeMillis() >= validTime) {
                logger.info("verify config valid time is expired: valid time: {}, now: {}", new Date(validTime), new Date(System.currentTimeMillis()));
                return;
            }
            isValid = true;
            logger.info("verify config: valid time: {}, verifyUserId: {}", new Date(validTime), verifyUserId);
        } catch (Exception e) {
            throw new IllegalArgumentException("url is illegal", e);
        }
    }

    public static Map<String, String> splitQuery(URL url) throws UnsupportedEncodingException, UnsupportedEncodingException {
        Map<String, String> query_pairs = new LinkedHashMap<String, String>();
        String query = url.getQuery();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
        }
        return query_pairs;
    }

    public void request(String method, String url, String body, Map<String, String> headers) {
        HttpUriRequestBase httpRequest = new HttpUriRequestBase(method.toUpperCase(), URI.create(url));
        logger.debug(body);
        CloseableHttpResponse response = null;
        String requestId = HttpUtils.getXRequestID();
        try {
            StringEntity entity = new StringEntity(body, ContentType.APPLICATION_JSON);
            httpRequest.addHeader(new BasicHeader("X-Request-ID", requestId));
            if (headers != null) {
                headers.entrySet().stream().forEach(
                        entry -> httpRequest.addHeader(new BasicHeader(entry.getKey(), entry.getValue())));
            }
            httpRequest.setEntity(entity);
            response = (CloseableHttpResponse) HttpUtils.getHttpClient().execute(httpRequest);
            String resultStr = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            if (!isSuccess(response, resultStr)) {
                String causeMsg = String.format("verify HTTP ERROR, code: %s, resultStr: %s", response.getCode(), resultStr);
                logger.error(causeMsg);
                logger.error(
                        "verify request error: requestId={}, method={}, url={}, ,header={}, body=\r\n{}", requestId, method,
                        url, headers, body);
            }
            logger.info("verify request success. result: {}", resultStr);
        } catch (Exception e) {
            String errorMsg = String.format("request error, parse error: requestId=%s, method=%s, url=%s, header={}, body=\r\n%s", requestId, method, url, headers, body);
            logger.error(errorMsg);
            throw new RuntimeException(errorMsg, e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                logger.error(e.toString(), e);
            }
        }
    }

    private static boolean isSuccess(CloseableHttpResponse response, String resultStr) throws IOException {
        if (HttpStatus.SC_OK != response.getCode()) {
            return false;
        }
        Map object = RangersJSONConfig.getInstance().fromJson(resultStr, Map.class);
        if (object != null &&
                object.containsKey("message") && "ok".equalsIgnoreCase(object.get("message").toString())) {
            logger.debug("Send Success.");
            logger.debug(resultStr);
            return true;
        }
        return false;
    }

}
