/*
 * Copyright 2020 Beijing Volcano Engine Technology Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.datarangers.collector;

import com.datarangers.asynccollector.CollectorContainer;
import com.datarangers.config.DataRangersSDKConfigProperties;
import com.datarangers.config.RangersJSONConfig;
import com.datarangers.message.Message;
import com.datarangers.message.MessageEnv;
import com.datarangers.util.HttpUtils;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author taojian
 */
public abstract class Collector implements EventCollector {

  private String appType;
  public static final Logger logger = LoggerFactory.getLogger("DatarangersLog");
  public static Executor httpRequestPool = null;
  public static ScheduledExecutorService scheduled = null;
  public static CollectorContainer collectorContainer;

  private boolean enable;
  protected DataRangersSDKConfigProperties properties;

  public Collector(String appType, DataRangersSDKConfigProperties properties) {
    enable = properties.isEnable();
    this.properties = properties;
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
    sendMessage = RangersJSONConfig.getInstance().toJson(message);
    if (collectorContainer.getMessageQueue() != null) {
      try {
        if (!collectorContainer.produce(message)) {
          //队列满了,需要保存到log
          logger.error("datarangers send Queue reach max length");
          HttpUtils.writeFailedMessage(sendMessage);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      HttpUtils.writeFailedMessage(sendMessage);
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
}
