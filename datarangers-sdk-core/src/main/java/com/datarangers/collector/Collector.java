/*
 * Copyright 2020 Beijing Volcano Engine Technology Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.datarangers.collector;

import com.datarangers.asynccollector.CollectorContainer;
import com.datarangers.asynccollector.Consumer;
import com.datarangers.config.*;
import com.datarangers.message.AppMessage;
import com.datarangers.message.Message;
import com.datarangers.message.MessageEnv;
import com.datarangers.sender.Callback;
import com.datarangers.sender.Callback.FailedData;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
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
  protected KafkaProducer kafkaProducer;

  public Collector(String appType, DataRangersSDKConfigProperties properties, Callback cb) {
    this.appType = appType;
    this.enable = properties.isEnable();
    this.properties = properties;
    this.callback = cb;
    this.properties.setCallback(this.getCallback());
    this.initKafkaProducer();
  }

  private void initKafkaProducer(){
    if(SdkMode.KAFKA != this.properties.getMode()){
      return;
    }
    // 设置过了就不需要再自己创建
    if(kafkaProducer != null){
      return;
    }
    kafkaProducer = createProducer(this.properties.getKafka());
  }

  private KafkaProducer<String, String> createProducer(KafkaConfig kafkaConfig) {
    Properties props = new Properties();
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    props.put("bootstrap.servers", kafkaConfig.getBootstrapServers());
    Map<String, Object> map = kafkaConfig.getProperties();
    if (map != null && (!map.isEmpty())) {
      props.putAll(map);
    }
    return new KafkaProducer<>(props);
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
    if(kafkaProducer != null){
      // 使用kafka的方式
      sendByKafka(message.getAppMessage());
      return;
    }
    sendMessage = RangersJSONConfig.getInstance().toJson(message.getAppMessage());
    if (this.properties.isSync()) {
      syncSendMessage(message, sendMessage);
    } else {
      asyncSendMessage(message, sendMessage);
    }
  }

  private void sendByKafka(AppMessage appMessage) {
    // kafka sender，header 添加固定的头
    appMessage.getHeader().setSource(Constants.SDK_SERVER);
    String sendMessage = RangersJSONConfig.getInstance().toJson(appMessage);
    try {
      ProducerRecord producerRecord = new ProducerRecord<>(properties.getKafka().getTopic(), sendMessage);
      kafkaProducer.send(producerRecord, new org.apache.kafka.clients.producer.Callback() {
        @Override
        public void onCompletion(RecordMetadata recordMetadata, Exception e) {
          if (e != null) {
            logger.error(String.format("kafka send message error. value: \r\n %s", sendMessage), e);
            getCallback().onFailed(new FailedData(sendMessage, e.getMessage(), e, false));
          }
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
      logger.error(String.format("kafka send message error. value: \r\n %s", sendMessage), e);
      getCallback().onFailed(new FailedData(sendMessage, e.getMessage(), e, false));
    }
  }

  private void syncSendMessage(Message message, String sendMessage) {
    try {
      this.consumer.flush(message);
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("sync send message error", e);
      getCallback().onFailed(new FailedData(sendMessage, e.getMessage(), e, false));
    }
  }

  private void asyncSendMessage(Message message, String sendMessage) {
    if (collectorContainer.getMessageQueue() != null) {
      try {
        collectorContainer.produce(message);
      } catch (Exception e) {
        e.printStackTrace();
        logger.error("async send message error", e);
        getCallback().onFailed(new FailedData(sendMessage, e.getMessage(), e, false));
      }
    } else {
      logger.error("getMessageQueue is null");
      getCallback().onFailed(new FailedData(sendMessage, "getMessageQueue is null", false));
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
