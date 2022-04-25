/*
 * Copyright 2020 Beijing Volcano Engine Technology Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.datarangers.collector;

import com.datarangers.asynccollector.CollectorContainer;
import com.datarangers.asynccollector.Consumer;
import com.datarangers.config.DataRangersSDKConfigProperties;
import com.datarangers.config.RangersJSONConfig;
import com.datarangers.message.Message;
import com.datarangers.message.MessageEnv;
import com.datarangers.sender.Callback;
import com.datarangers.sender.Callback.FailedData;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledExecutorService;

/**
 * @author taojian
 */
public abstract class Collector implements EventCollector {

  private String appType;
  public static final Logger logger = LoggerFactory.getLogger("DatarangersLog");
  public static ExecutorService httpRequestPool = null;
  public static ScheduledExecutorService scheduled = null;
  public static CollectorContainer collectorContainer;

  private boolean enable;
  protected DataRangersSDKConfigProperties properties;
  protected Callback callback;
  protected Consumer consumer = null;

  public Collector(String appType, DataRangersSDKConfigProperties properties, Callback cb) {
    this.appType = appType;
    this.enable = properties.isEnable();
    this.properties = properties;
    this.callback = cb;
    this.properties.setCallback(this.getCallback());
  }

  public String getAppType() {
    return appType;
  }

  public Collector setAppType(String appType) {
    this.appType = appType;
    return this;
  }

  public void send(Message message) {
    sendMessage(message);
  }

  protected void sendMessage(Message message) {
    if (!enable) {
      return;
    }
    message.merge();
    String sendMessage;

    validate(message);
    sendMessage = RangersJSONConfig.getInstance().toJson(message.getAppMessage());
    if (this.properties.isSync()) {
      syncSendMessage(message, sendMessage);
    } else {
      asyncSendMessage(message, sendMessage);
    }
  }

  private void syncSendMessage(Message message, String sendMessage) {
    try {
      this.consumer.flush(message);
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("sync send message error", e);
      getCallback().onFailed(new FailedData(sendMessage, e.getMessage(), e));
    }
  }

  private void asyncSendMessage(Message message, String sendMessage) {
    if (collectorContainer.getMessageQueue() != null) {
      try {
        collectorContainer.produce(message);
      } catch (Exception e) {
        e.printStackTrace();
        logger.error("async send message error", e);
        getCallback().onFailed(new FailedData(sendMessage, e.getMessage(), e));
      }
    } else {
      logger.error("getMessageQueue is null");
      getCallback().onFailed(new FailedData(sendMessage, "getMessageQueue is null"));
    }
  }

  /**
   * message 检查
   */
  private void validate(Message message) {
    // 当前只有saas需要校验下appkey
    if (this.properties.getMessageEnv() != MessageEnv.SAAS) {
      return;
    }

    Integer appId = message.getAppMessage().getAppId();
    Map<Integer, String> appKeys = this.properties.getAppKeys();
    String appKey = appKeys.get(appId);
    if (appKey == null) {
      throw new IllegalArgumentException("App key cannot be empty. app_id: " + appId);
    }
  }

  public Callback getCallback() {
    return callback;
  }

  public void setCallback(Callback callback) {
    this.callback = callback;
  }

  public Consumer getConsumer() {
    return consumer;
  }

  public void setConsumer(Consumer consumer) {
    this.consumer = consumer;
  }
}
