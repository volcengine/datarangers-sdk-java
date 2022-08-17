/*
 * Copyright 2020 Beijing Volcano Engine Technology Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.datarangers.config;

import com.datarangers.asynccollector.*;
import com.datarangers.collector.Collector;
import com.datarangers.logger.RangersFileCleaner;
import com.datarangers.message.MessageEnv;
import com.datarangers.sender.Callback;
import com.datarangers.sender.callback.LoggingCallback;
import com.datarangers.util.HttpUtils;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author hTangle
 */
public class DataRangersSDKConfigProperties {

  public static final Logger logger = LoggerFactory.getLogger(DataRangersSDKConfigProperties.class);
  public Map<String, String> headers;
  public String domain;
  public int threadPoolCount = 1;
  public int maxPoolSize = 8;
  public int corePoolSize = 4;

  /**
   * 该配置过期，请使用httpConfig的配置
   */
  @Deprecated
  public int httpTimeout = 500;

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
  public int threadCount = 1;
  public int queueSize = 10240;
  public boolean send = true;

  private boolean sendBatch = false;
  private int batchSize = 20;
  private int waitTimeMs = 100;

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

  public boolean enable = true;

  public String eventSavePath = "logs/";
  public List<String> eventFilePaths;
  public String eventSaveName = "datarangers.log";
  public int eventSaveMaxHistory = 20;
  public int eventSaveMaxFileSize = 100;
  public int eventSaveMaxDays = 5;

  public CollectorQueue userQueue;

  public boolean hasConsumer = true;
  public boolean hasProducer = true;

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

  public boolean isHasConsumer() {
    return hasConsumer;
  }

  public DataRangersSDKConfigProperties setHasConsumer(boolean hasConsumer) {
    this.hasConsumer = hasConsumer;
    return this;
  }

  public boolean isHasProducer() {
    return hasProducer;
  }

  public DataRangersSDKConfigProperties setHasProducer(boolean hasProducer) {
    this.hasProducer = hasProducer;
    return this;
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

  public int getEventSaveMaxHistory() {
    return eventSaveMaxHistory;
  }

  public DataRangersSDKConfigProperties setEventSaveMaxHistory(int eventSaveMaxHistory) {
    this.eventSaveMaxHistory = eventSaveMaxHistory;
    return this;
  }

  public int getEventSaveMaxFileSize() {
    return eventSaveMaxFileSize;
  }

  public DataRangersSDKConfigProperties setEventSaveMaxFileSize(int eventSaveMaxFileSize) {
    this.eventSaveMaxFileSize = eventSaveMaxFileSize;
    return this;
  }

  public boolean isSend() {
    return send;
  }

  public DataRangersSDKConfigProperties setSend(boolean send) {
    this.send = send;
    return this;
  }

  public int getThreadCount() {
    return threadCount;
  }

  public void setThreadCount(int threadCount) {
    this.threadCount = threadCount;
  }

  public int getQueueSize() {
    return queueSize;
  }

  public void setQueueSize(int queueSize) {
    this.queueSize = queueSize;
  }

  public int getMaxPoolSize() {
    return maxPoolSize;
  }

  public void setMaxPoolSize(int maxPoolSize) {
    this.maxPoolSize = maxPoolSize;
  }

  public int getCorePoolSize() {
    return corePoolSize;
  }

  public void setCorePoolSize(int corePoolSize) {
    this.corePoolSize = corePoolSize;
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

  public int getThreadPoolCount() {
    return threadPoolCount;
  }

  public void setThreadPoolCount(int threadPoolCount) {
    this.threadPoolCount = threadPoolCount;
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

  public void setLogger() {
    Consumer.setWriterPool(getEventFilePaths(), getEventSaveName(), getEventSaveMaxFileSize());
    if (callback == null) {
      setCallback(new LoggingCallback(getEventSavePath(), "error-" + getEventSaveName(),
          getEventSaveMaxFileSize()));
    }
  }

  public void setCommon() {
    EventConfig.saveFlag = SdkMode.FILE == getMode();
    EventConfig.sendFlag = SdkMode.HTTP == getMode();
    if (EventConfig.sendFlag) {
      httpConfig = this.getHttpConfig();
      if (httpConfig.getMaxPerRoute() < this.getThreadCount()) {
        httpConfig.setMaxPerRoute(this.getThreadCount());
      }
      if (httpConfig.getMaxTotal() < httpConfig.getMaxPerRoute()) {
        httpConfig.setMaxTotal(httpConfig.getMaxPerRoute());
      }
      httpConfig.initTimeOut(getHttpTimeout());
      HttpUtils
          .createHttpClient(this.getHttpConfig(), this.getCustomHttpClient(), this.getCallback());
      if (EventConfig.SEND_HEADER == null) {
        EventConfig.SEND_HEADER = getHeaders();
        EventConfig.SEND_HEADER.put("User-Agent", "DataRangers Java SDK");
        EventConfig.SEND_HEADER.put("Content-Type", "application/json");
        List<Header> headerList = new ArrayList<>();
        EventConfig.SEND_HEADER
            .forEach((key, value) -> headerList.add(new BasicHeader(key, value)));
        EventConfig.headers = headerList.toArray(new Header[0]);
      }
    }
    setConsumer(getThreadCount());
    EventConfig.setUrl(getDomain());
  }


  public ExecutorService setThreadPool() {
    return Executors.newFixedThreadPool(getCorePoolSize());
  }

  public void setConsumer(int threadCount) {
    if (EventConfig.saveFlag) {
      threadCount = 1;
      logger.info("Start LogAgent Mode");
    } else {
      logger.info("Start Http Mode");
    }
    if (userQueue == null) {
      Collector.collectorContainer = new CollectorContainer(
          RangersCollectorQueue.getInstance(getQueueSize()));
    } else {
      //如果客户自定义了queue，则需要替换为客户自定义queue
      Collector.collectorContainer = new CollectorContainer(userQueue);
    }
    if ((!sync) && hasConsumer && Collector.httpRequestPool == null) {
      //有消费者才初始化消费者
      Collector.httpRequestPool = setThreadPool();
      for (int i = 0; i < threadCount; i++) {//必须全部消费同一个队列
        Collector.httpRequestPool.execute(new Consumer(Collector.collectorContainer, this));
      }
    }
    if ((!sync) && hasProducer) {
      //有生产者才需要记录
      Collector.scheduled = Executors.newSingleThreadScheduledExecutor();
      Collector.scheduled
          .scheduleAtFixedRate(new CollectorCounter(getEventSavePath()), 0, 2, TimeUnit.MINUTES);
      if (EventConfig.saveFlag) {
        Collector.scheduled.scheduleAtFixedRate(
            new RangersFileCleaner(getEventFilePaths(), getEventSaveName(), getEventSaveMaxDays()),
            0, 12, TimeUnit.HOURS);
      }
    }
    logger.info("Start DataRangers Cleaner/Record Thread");
  }


  public static volatile Boolean IS_INIT = false;

  public void init() {
    if (!IS_INIT) {
      synchronized (DataRangersSDKConfigProperties.class) {
        if (!IS_INIT) {
          setLogger();
          setCommon();
          setHook();
          IS_INIT = true;
        }
      }
    }
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
    if ("saas".equalsIgnoreCase(getEnv()) || SAAS_DOMAIN_URLS.contains(getDomain())) {
      return MessageEnv.SAAS;
    }
    return MessageEnv.PRIVATIZATION;
  }

  public Map<Integer, String> getAppKeys() {
    return appKeys;
  }

  public void setAppKeys(Map<Integer, String> appKeys) {
    this.appKeys = appKeys;
  }

  private void setHook() {
    DataRangersSDKConfigProperties properties = this;
    Runtime.getRuntime().addShutdownHook(new Thread(
        () -> {
          if(Collector.httpRequestPool != null){
            Collector.httpRequestPool.shutdown();
          }

          new Consumer(Collector.collectorContainer, properties).flush();
        }));
  }

}

