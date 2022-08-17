/*
 * Copyright 2020 Beijing Volcano Engine Technology Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.datarangers.util;

import com.datarangers.config.HttpConfig;
import com.datarangers.config.RangersJSONConfig;
import com.datarangers.sender.Callback;
import com.datarangers.sender.Callback.FailedData;

import java.io.File;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.DefaultHttpRequestRetryStrategy;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.TrustAllStrategy;
import org.apache.hc.client5.http.ssl.TrustSelfSignedStrategy;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicHeader;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.ssl.TrustStrategy;
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
    private static Callback callback = null;

    private static HttpClient httpClient = null;
    private static volatile Boolean initFlag = false;

    public static void createHttpClient(
            HttpConfig httpConfig, HttpClient customHttpClient, Callback callback) {
        if (!initFlag) {
            synchronized (initFlag) {
                if (!initFlag) {
                    if (httpClient == null) {
                        if (customHttpClient != null) {
                            httpClient = customHttpClient;
                        } else {
                            SSLContext sslContext = createSSLContext(httpConfig);
                            SSLConnectionSocketFactory sslSocketFactory;
                            if (httpConfig.isTrustDisable()) {
                                sslSocketFactory =
                                        new SSLConnectionSocketFactory(sslContext, (s, sslSession) -> true);
                            } else {
                                sslSocketFactory = SSLConnectionSocketFactory.getSocketFactory();
                            }

                            PoolingHttpClientConnectionManager cm = PoolingHttpClientConnectionManagerBuilder
                                    .create()
                                    .setMaxConnTotal(httpConfig.getMaxTotal())
                                    .setMaxConnPerRoute(httpConfig.getMaxPerRoute())
                                    .setSSLSocketFactory(sslSocketFactory)
                                    .build();
                            RequestConfig requestConfig = RequestConfig.custom()
                                    .setConnectionRequestTimeout(
                                            Timeout.ofMicroseconds(httpConfig.getRequestTimeout()))
                                    .setConnectTimeout(Timeout.ofMilliseconds(httpConfig.getConnectTimeout()))
                                    .setResponseTimeout(Timeout.ofMilliseconds(httpConfig.getSocketTimeout()))
                                    .setConnectionKeepAlive(TimeValue.ofSeconds(httpConfig.getKeepAliveTimeout()))
                                    .setCookieSpec("easy")
                                    .build();
                            httpClient = HttpClients.custom()
                                    .setRetryStrategy(
                                            new DefaultHttpRequestRetryStrategy(3, TimeValue.ofMilliseconds(1000)))
                                    .setConnectionManager(cm)
                                    .setDefaultRequestConfig(requestConfig).build();
                        }
                    }
                    initFlag = true;
                    HttpUtils.callback = callback;
                }
            }
        }
    }

    private static SSLContext createSSLContext(
            HttpConfig httpConfig) {
        if (httpConfig.isTrustDisable()) {
            SSLContext sslContext = null;
            try {
                sslContext = SSLContext.getInstance("TLS");
                TrustManager tm = new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType)
                            throws CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType)
                            throws CertificateException {
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                };
                sslContext.init((KeyManager[]) null, new TrustManager[]{tm}, (SecureRandom) null);
                return sslContext;
            } catch (NoSuchAlgorithmException | KeyManagementException e) {
                e.printStackTrace();
                throw new RuntimeException("init ssl error", e);
            }
        }
        if (!httpConfig.isCustomKeyTrustEnable()) {
            return SSLContexts.createDefault();
        }
        SSLContextBuilder sslContextBuilder = SSLContextBuilder.create();
        String keyMaterialPath = httpConfig.getKeyMaterialPath();
        String keyPassword = httpConfig.getKeyPassword();
        String storePassword = httpConfig.getStorePassword();
        String trustMaterialPath = httpConfig.getTrustMaterialPath();
        TrustStrategy trustStrategy =
                "self".equalsIgnoreCase(httpConfig.getTrustStrategy())
                        ? new TrustSelfSignedStrategy() : new TrustAllStrategy();
        try {
            sslContextBuilder.loadKeyMaterial(new File(keyMaterialPath), storePassword.toCharArray(),
                    keyPassword.toCharArray());
            sslContextBuilder
                    .loadTrustMaterial(new File(trustMaterialPath), storePassword.toCharArray(),
                            trustStrategy);
            return sslContextBuilder.build();
        } catch (NoSuchAlgorithmException | KeyStoreException | UnrecoverableKeyException | CertificateException |
                 IOException | KeyManagementException e) {
            e.printStackTrace();
            throw new RuntimeException("init ssl error", e);
        }
    }


    public static void post(String url, String body, Map<String, String> headers, int count) {
        request("POST", url, body, headers, count);
    }

    public static void request(String method, String url, String body, Map<String, String> headers) {
        if (body == null) {
            return;
        }
        request(method, url, body, headers, 1);
    }

    public static void request(String method, String url, String body, Map<String, String> headers,
                               int count) {
        HttpUriRequestBase httpRequest = new HttpUriRequestBase(method.toUpperCase(), URI.create(url));
        logger.debug(body);
        CloseableHttpResponse response = null;
        String requestId = getXRequestID();
        String resultStr = null;
        try {
            StringEntity entity = new StringEntity(body, ContentType.APPLICATION_JSON);
            httpRequest.addHeader(new BasicHeader("X-Request-ID", requestId));
            if (headers != null) {
                headers.entrySet().stream().forEach(
                        entry -> httpRequest.addHeader(new BasicHeader(entry.getKey(), entry.getValue())));
            }
            httpRequest.setEntity(entity);
            response = (CloseableHttpResponse) httpClient.execute(httpRequest);
            resultStr = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            if (!isSuccess(response, resultStr)) {
                String causeMsg = String.format("HTTP ERROR, code: %s, resultStr: %s", response.getCode(), resultStr);
                logger.error(causeMsg);
                logger.error(
                        "request error: requestId={}, method={}, url={}, body={},header={}", requestId, method,
                        url, body,
                        headers);
                callback.onFailed(new FailedData(body, causeMsg, isListable(url)));
            }
        } catch (IOException e) {
            if (count > 2) {
                logger.error(String.format("request error, io error: %s, resultStr: %s", e.getMessage(), resultStr), e);
                callback.onFailed(new FailedData(body, e.toString(), e, isListable(url)));
            } else {
                count++;
                post(url, body, headers, count);
            }
        } catch (ParseException e) {
            logger.error(
                    "request error, parse error: requestId={}, method={}, url={}, body={},header={}", requestId, method,
                    url, body,
                    headers);
            callback.onFailed(new FailedData(body, e.toString(), e, isListable(url)));
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

    public static void post(String url, String body, Map<String, String> headers) {
        if (body == null) {
            return;
        }
        post(url, body, headers, 1);
    }

    public static String getXRequestID() {
        return UUID.randomUUID().toString()
                .replaceAll("-", String.valueOf(new SecureRandom().nextInt(10)));
    }

    private static boolean isSuccess(CloseableHttpResponse response, String resultStr) throws IOException {
        if (HttpStatus.SC_OK != response.getCode()) {
            return false;
        }
        Map object = RangersJSONConfig.getInstance().fromJson(resultStr, Map.class);
        if (object != null &&
                (object.containsKey("message") && "success".equals(object.get("message").toString())
                        || (object.containsKey("responses")))) {
            logger.debug("Send Success.");
            logger.debug(resultStr);
            return true;
        }
        return false;
    }

    /**
     * 是否是list
     * @return
     */
    private static boolean isListable(String url){
        return url.endsWith("/sdk/list");
    }

}

