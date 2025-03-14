/*
 * Copyright 2020 Beijing Volcano Engine Technology Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.datarangers.config;

import com.datarangers.asynccollector.CollectorQueue;
import com.datarangers.message.MessageEnv;
import com.datarangers.sender.Callback;
import org.apache.hc.client5.http.classic.HttpClient;

import java.time.ZoneOffset;
import java.util.*;

/**
 * @author hTangle
 */
public class DataRangersSDKConfigProperties {
  public boolean enable = true;
  public Map<String, String> headers;
  public String domain;

  private String path;
  private String batchPath;

  private int threadCount = 20;

  /**
   * 该配置过期，请使用httpConfig的配置
   */
  @Deprecated
  public int httpTimeout = 10000;

  public String timeZone = "+8";
  public ZoneOffset timeOffset = null;

  /**
   * 过期的配置，使用mode配置，支持：
   * file: 保存到本地
   * http: 直接http发送
   * kafka: 通过kafka进行发送
   */
  @Deprecated
  public boolean save = false;

  public int queueSize = 10240;

  /**
   * batch批量配置
   */
  private boolean sendBatch = false;
  private int batchSize = 20;
  private int waitTimeMs = 100;

  /**
   * file模式 相关配置
   */
  public String eventSavePath = "logs/";
  public List<String> eventFilePaths;
  public String eventSaveName = "datarangers.log";
  public int eventSaveMaxFileSize = 100;
  private boolean eventCountFileDisable = false;

  // 日志清理时间
  public int eventSaveMaxDays = -1;

  public CollectorQueue userQueue;

  // http相关配置
  private HttpConfig httpConfig = new HttpConfig();

  private HttpClient customHttpClient;

  private Map<Integer, String> appKeys = new HashMap<>();

  /**
   * saas 或者 privatization(私有化)
   */
  private String env = "privatization";

  private List<String> SAAS_DOMAIN_URLS = Arrays.asList(
          "https://mcs.ctobsnssdk.com",
          "https://mcs.tobsnssdk.com");

  /**
   * 云原生的配置
   */
  private List<String> SAAS_NATIVE_DOMAIN_URLS = Arrays.asList(
          "https://gator.volces.com");

  /**
   * saas openapi 配置地址
   */
  private OpenapiConfig openapiConfig = new OpenapiConfig();

  private Callback callback;

  /**
   * 同步还是异步发送，默认是异步发送
   */
  private boolean sync;

  private SdkMode mode;
  private KafkaConfig kafka;

  private VerifyConfig verify = new VerifyConfig();

  public VerifyConfig getVerify() {
    return verify;
  }

  public void setVerify(VerifyConfig verify) {
    this.verify = verify;
  }

  public boolean isSendBatch() {
    return sendBatch;
  }

  public void setSendBatch(boolean sendBatch) {
    this.sendBatch = sendBatch;
  }

  public int getBatchSize() {
    return batchSize;
  }

  public void setBatchSize(int batchSize) {
    this.batchSize = batchSize;
  }

  public int getWaitTimeMs() {
    return waitTimeMs;
  }

  public void setWaitTimeMs(int waitTimeMs) {
    this.waitTimeMs = waitTimeMs;
  }

  public boolean isEnable() {
    return enable;
  }

  public void setEnable(boolean enable) {
    this.enable = enable;
  }

  public KafkaConfig getKafka() {
    return kafka;
  }

  public void setKafka(KafkaConfig kafka) {
    this.kafka = kafka;
  }

  public SdkMode getMode() {
    if (this.mode != null) {
      return mode;
    }
    if (isSave()) {
      return SdkMode.FILE;
    }
    return SdkMode.HTTP;
  }

  public void setMode(SdkMode mode) {
    this.mode = mode;
  }

  public boolean isSync() {
    return sync;
  }

  public void setSync(boolean sync) {
    this.sync = sync;
  }

  public Callback getCallback() {
    return callback;
  }

  public void setCallback(Callback callback) {
    this.callback = callback;
  }

  public int getHttpTimeout() {
    return httpTimeout;
  }

  public DataRangersSDKConfigProperties setHttpTimeout(int httpTimeout) {
    this.httpTimeout = httpTimeout;
    return this;
  }

  public List<String> getEventFilePaths() {
    if (eventFilePaths == null) {
      eventFilePaths = new ArrayList<>();
      eventFilePaths.add(eventSavePath);
    }
    return eventFilePaths;
  }

  public int getEventSaveMaxDays() {
    return eventSaveMaxDays;
  }

  public DataRangersSDKConfigProperties setEventSaveMaxDays(int eventSaveMaxDays) {
    this.eventSaveMaxDays = eventSaveMaxDays;
    return this;
  }

  public DataRangersSDKConfigProperties setEventFilePaths(List<String> eventFilePaths) {
    this.eventFilePaths = eventFilePaths;
    return this;
  }

  public String getEventSavePath() {
    return eventSavePath;
  }

  public DataRangersSDKConfigProperties setEventSavePath(String eventSavePath) {
    this.eventSavePath = eventSavePath;
    return this;
  }

  public String getEventSaveName() {
    return eventSaveName;
  }

  public DataRangersSDKConfigProperties setEventSaveName(String eventSaveName) {
    this.eventSaveName = eventSaveName;
    return this;
  }

  public int getEventSaveMaxFileSize() {
    return eventSaveMaxFileSize;
  }

  public DataRangersSDKConfigProperties setEventSaveMaxFileSize(int eventSaveMaxFileSize) {
    this.eventSaveMaxFileSize = eventSaveMaxFileSize;
    return this;
  }

  public boolean isEventCountFileDisable() {
    return eventCountFileDisable;
  }

  public void setEventCountFileDisable(boolean eventCountFileDisable) {
    this.eventCountFileDisable = eventCountFileDisable;
  }

  public int getQueueSize() {
    return queueSize;
  }

  public void setQueueSize(int queueSize) {
    this.queueSize = queueSize;
  }

  public int getThreadCount() {
    return threadCount;
  }

  public void setThreadCount(int threadCount) {
    this.threadCount = threadCount;
  }

  public Map<String, String> getHeaders() {
    if (headers == null) {
      headers = new HashMap<>();
    }
    return headers;
  }

  public void setHeaders(Map<String, String> headers) {
    this.headers = headers;
  }

  public String getDomain() {
    return domain;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getBatchPath() {
    return batchPath;
  }

  public void setBatchPath(String batchPath) {
    this.batchPath = batchPath;
  }

  public ZoneOffset getTimeOffset() {
    return timeOffset;
  }

  public void setTimeOffset(ZoneOffset timeOffset) {
    this.timeOffset = timeOffset;
  }

  @Deprecated
  public boolean isSave() {
    return save;
  }

  @Deprecated
  public void setSave(boolean save) {
    this.save = save;
  }

  public String getTimeZone() {
    return timeZone;
  }

  public void setTimeZone(String timeZone) {
    timeOffset = ZoneOffset.of(timeZone);
    this.timeZone = timeZone;
  }

  public CollectorQueue getUserQueue() {
    return userQueue;
  }

  public DataRangersSDKConfigProperties setUserQueue(CollectorQueue userQueue) {
    this.userQueue = userQueue;
    return this;
  }

  public HttpConfig getHttpConfig() {
    return httpConfig;
  }

  public void setHttpConfig(HttpConfig httpConfig) {
    this.httpConfig = httpConfig;
  }

  public HttpClient getCustomHttpClient() {
    return customHttpClient;
  }

  public void setCustomHttpClient(HttpClient customHttpClient) {
    this.customHttpClient = customHttpClient;
  }

  public String getEnv() {
    return env;
  }

  public void setEnv(String env) {
    this.env = env;
  }

  public OpenapiConfig getOpenapiConfig() {
    return openapiConfig;
  }

  public void setOpenapiConfig(OpenapiConfig openapiConfig) {
    this.openapiConfig = openapiConfig;
  }

  public MessageEnv getMessageEnv() {
    if (Constants.ENV_SAAS.equalsIgnoreCase(getEnv()) || SAAS_DOMAIN_URLS.contains(getDomain())) {
      return MessageEnv.SAAS;
    }
    if (Constants.ENV_SAAS_NATIVE.equalsIgnoreCase(getEnv()) || SAAS_NATIVE_DOMAIN_URLS.contains(getDomain())) {
      return MessageEnv.SAAS_NATIVE;
    }
    return MessageEnv.PRIVATIZATION;
  }

  public Map<Integer, String> getAppKeys() {
    return appKeys;
  }

  public void setAppKeys(Map<Integer, String> appKeys) {
    this.appKeys = appKeys;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(String.format("enable: %s \r\n", enable));
    sb.append(String.format("env: %s \r\n", getMessageEnv()));
    sb.append(String.format("sync: %s \r\n", sync));
    sb.append(String.format("sdkMode: %s \r\n", mode));
    sb.append(String.format("domain: %s \r\n", domain));
    sb.append(String.format("headers: %s \r\n", headers));
    sb.append(String.format("threadCount: %s \r\n", threadCount));
    sb.append(String.format("queueSize: %s \r\n", queueSize));
    sb.append(String.format("sendBatch: %s \r\n", sendBatch));
    sb.append(String.format("batchSize: %s \r\n", batchSize));
    sb.append(String.format("waitTimeMs: %s \r\n", waitTimeMs));
    sb.append(String.format("httpConfig: %s \r\n", httpConfig));
    sb.append(String.format("eventSavePath: %s \r\n", eventSavePath));
    sb.append(String.format("eventSaveName: %s \r\n", eventSaveName));
    sb.append(String.format("eventFilePaths: %s \r\n", eventFilePaths));
    sb.append(String.format("eventSaveMaxFileSize: %s \r\n", eventSaveMaxFileSize));
    sb.append(String.format("eventSaveMaxDays: %s \r\n", eventSaveMaxDays));
    sb.append(String.format("eventCountFileDisable: %s \r\n", eventCountFileDisable));
    sb.append(String.format("verify: %s\r\n", verify));
    sb.append(String.format("openapiConfig: %s \r\n", openapiConfig));
    sb.append(String.format("kafka: %s", kafka));
    return sb.toString();
  }
}

