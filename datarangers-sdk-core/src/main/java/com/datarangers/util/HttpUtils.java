/*
 * Copyright 2020 Beijing Volcano Engine Technology Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.datarangers.util;

import com.datarangers.config.EventConfig;
import com.datarangers.config.RangersJSONConfig;
import com.datarangers.logger.RangersLoggerWriter;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.DefaultHttpRequestRetryStrategy;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicHeader;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Map;
import java.util.UUID;

public class HttpUtils {
    public static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);
    private static RangersLoggerWriter writer;

    public static void setWriter(final String targetPrefix, final String targetName, int maxSize) {
        if (writer == null)
            writer = RangersLoggerWriter.getInstance(targetPrefix, targetName, maxSize);
    }

    public static void writeFailedMessage(String message) {
        synchronized (writer) {
            writer.write(message + "\n");
        }
    }

    private static final Integer MAX_TOTAL = 1000;             //连接池最大连接数
    private static final Integer MAX_PER_ROUTE = 100;          //单个路由默认最大连接数
    private static Integer REQ_TIMEOUT = 2 * 500;     //请求超时时间ms
    private static Integer CONN_TIMEOUT = 2 * 500;     //连接超时时间ms
    private static Integer SOCK_TIMEOUT = 4 * 500;    //读取超时时间ms

    public static void setRequestTimeOut(int timeBase) {
        REQ_TIMEOUT = timeBase;
        CONN_TIMEOUT = timeBase;
        SOCK_TIMEOUT = timeBase * 2;
    }

    private static HttpClient httpClient = null;
    private static Boolean initFlag = false;

    public static void createHttpClient() {
        if (!initFlag) {
            synchronized (initFlag) {
                if (!initFlag) {
                    if (httpClient == null) {
                        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
                        cm.setMaxTotal(MAX_TOTAL);
                        cm.setDefaultMaxPerRoute(MAX_PER_ROUTE);
                        RequestConfig requestConfig = RequestConfig.custom()
                                .setConnectionRequestTimeout(Timeout.ofMicroseconds(REQ_TIMEOUT))
                                .setConnectTimeout(Timeout.ofMilliseconds(CONN_TIMEOUT))

                                .setResponseTimeout(Timeout.ofMilliseconds(SOCK_TIMEOUT))
                                .setCookieSpec("easy")
                                .build();
                        httpClient = HttpClients.custom().setRetryStrategy(new DefaultHttpRequestRetryStrategy(3, TimeValue.ofMilliseconds(1000))).setConnectionManager(cm).setDefaultRequestConfig(requestConfig).build();
                    }
                    initFlag = true;
                }
            }
        }
    }

    public static void post(String url, String body, Map<String, String> headers, int count) {
        HttpPost httpPost = new HttpPost(url);
        logger.debug(body);
        CloseableHttpResponse response = null;
        String requestId = getXRequestID();
        Map object = null;
        try {
            StringEntity entity = new StringEntity(body, ContentType.APPLICATION_JSON);
            httpPost.setHeaders(EventConfig.headers);
            httpPost.addHeader(new BasicHeader("X-Request-ID", requestId));
            httpPost.setEntity(entity);
            response = (CloseableHttpResponse) httpClient.execute(httpPost);
            String resultStr = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            object = RangersJSONConfig.getInstance().fromJson(resultStr, Map.class);
            if (object != null &&
                    (object.containsKey("message") && "success".equals(object.get("message").toString())
                            || (object.containsKey("responses")))) {
                logger.debug("Send Success:" + url);
                logger.debug(resultStr);
            } else {
                logger.error("HTTP ERROR: " + response.getEntity().toString());
                logger.error("requestId=" + requestId + ",url=" + url + ",body=" + body + ",header=" + headers + ",result:" + resultStr);
                writeFailedMessage(body);
            }
        } catch (IOException e) {
            if (count > 2) {
                logger.error(e.toString());
                writeFailedMessage(body);
            } else {
                count++;
                post(url, body, headers, count);
            }
        } catch (ParseException e) {
            logger.error("Parse Json error:requestId=" + requestId + ",url=" + url + ",body=" + body + ",header=" + headers);
            writeFailedMessage(body);
        } finally {
            try {
                if (response != null) response.close();
            } catch (IOException e) {
                logger.error(e.toString());
            }
        }
    }

    public static void post(String url, String body, Map<String, String> headers) {
        if (body == null) return;
        post(url, body, headers, 1);
    }

    public static String getXRequestID() {
        return UUID.randomUUID().toString()
                .replaceAll("-", String.valueOf(new SecureRandom().nextInt(10)));
    }
}

