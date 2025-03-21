package com.datarangers.sender.saas;

import com.datarangers.config.Constants;
import com.datarangers.config.DataRangersSDKConfigProperties;
import com.datarangers.config.EventConfig;
import com.datarangers.config.RangersJSONConfig;
import com.datarangers.message.Message;
import com.datarangers.message.saas.SaasServerAppMessage;
import com.datarangers.sender.MessageSender;
import com.datarangers.util.HttpUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author zhangpeng.spin@bytedance.com
 * @Date 2021-07-22
 */
public class SaasServerAppMessageSender implements MessageSender {

    private static final String PATH = "/v2/event/json";

    private static final String BATCH_PATH = "/v2/event/list";

    @Override
    public void send(Message message, DataRangersSDKConfigProperties sdkConfigProperties) {
        Object sendMessage = new SaasServerAppMessage(message);
        String url = sdkConfigProperties.getDomain() + getPath(sdkConfigProperties.getPath(), PATH);
        Map<String, String> headers = new HashMap<>();
        headers.putAll(EventConfig.SEND_HEADER);
        Integer appId = message.getAppMessage().getAppId();
        Map<Integer, String> appKeys = sdkConfigProperties.getAppKeys();
        String appKey = appKeys.get(appId);
        if (appKey == null) {
            throw new IllegalArgumentException("App key cannot be empty. app_id: " + appId);
        }
        headers.put(Constants.APP_KEY, appKey);
        HttpUtils.post(url, RangersJSONConfig.getInstance().toJson(sendMessage), headers);
    }

    @Override
    public void sendBatch(List<Message> messages, DataRangersSDKConfigProperties sdkConfigProperties) {
        List<SaasServerAppMessage> sendMessages = messages.stream().map(n -> new SaasServerAppMessage(n))
                .collect(Collectors.toList());
        String url = sdkConfigProperties.getDomain() + getPath(sdkConfigProperties.getBatchPath(), BATCH_PATH);

        Map<String, String> headers = new HashMap<>();
        headers.putAll(EventConfig.SEND_HEADER);
        Integer appId = messages.get(0).getAppMessage().getAppId();
        Map<Integer, String> appKeys = sdkConfigProperties.getAppKeys();
        String appKey = appKeys.get(appId);
        if (appKey == null) {
            throw new IllegalArgumentException("App key cannot be empty. app_id: " + appId);
        }
        headers.put(Constants.APP_KEY, appKey);
        String body = RangersJSONConfig.getInstance().toJson(sendMessages);
        HttpUtils.post(url, body, headers);
    }

    protected String getPath(String domainPath, String defaultPath) {
        if (domainPath == null || domainPath.trim().length() == 0) {
            return defaultPath;
        }
        if (!domainPath.startsWith("/")) {
            domainPath = "/" + domainPath;
        }
        return domainPath;
    }
}
