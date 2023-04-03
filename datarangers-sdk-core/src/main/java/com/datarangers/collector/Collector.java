/*
 * Copyright 2020 Beijing Volcano Engine Technology Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.datarangers.collector;

import com.datarangers.asynccollector.*;
import com.datarangers.config.*;
import com.datarangers.logger.RangersFileCleaner;
import com.datarangers.message.AppMessage;
import com.datarangers.message.Message;
import com.datarangers.message.MessageEnv;
import com.datarangers.sender.Callback;
import com.datarangers.sender.Callback.FailedData;
import com.datarangers.sender.VerifySender;
import com.datarangers.sender.callback.LoggingCallback;
import com.datarangers.util.HttpUtils;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.message.BasicHeader;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author taojian
 */
public abstract class Collector implements EventCollector {

    private String appType;
    public static final Logger logger = LoggerFactory.getLogger("DatarangersLog");
    public static ExecutorService executorService = null;
    public static ScheduledExecutorService scheduled = null;
    public static CollectorContainer collectorContainer;
    public static VerifySender verifySender;
    private boolean enable;
    protected DataRangersSDKConfigProperties properties;
    protected Callback callback;
    protected Consumer consumer = null;
    protected static KafkaProducer kafkaProducer;
    private static volatile Boolean IS_INIT = false;

    public Collector(String appType, DataRangersSDKConfigProperties properties, Callback cb) {
        this.appType = appType;
        this.enable = properties.isEnable();
        this.properties = properties;
        this.callback = cb;
        this.properties.setCallback(this.getCallback());
        this.init();
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
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


    private void initModeKafka() {
        if (SdkMode.KAFKA != this.properties.getMode()) {
            return;
        }
        logger.info("init kafka producer");
        // 设置过了就不需要再自己创建
        if (kafkaProducer != null) {
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

    public void send(Message message) {
        sendMessage(message);
    }

    private void sendMessage(Message message) {
        if (!enable) {
            return;
        }
        message.merge();
        String sendMessage;

        validate(message);
        // 埋点实时检测发送
        sendToVerify(message);

        if (kafkaProducer != null) {
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

    private void sendToVerify(Message message){
        verifySender.send(message);
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
        // 当前只有saas,saas_native需要校验下appkey
        if (!Arrays.asList(MessageEnv.SAAS, MessageEnv.SAAS_NATIVE)
                .contains(this.properties.getMessageEnv())) {
            return;
        }

        Integer appId = message.getAppMessage().getAppId();
        Map<Integer, String> appKeys = this.properties.getAppKeys();
        String appKey = appKeys.get(appId);
        if (appKey == null) {
            throw new IllegalArgumentException("App key cannot be empty. app_id: " + appId);
        }
    }

    /**
     * 配置初始化
     */
    private void init() {
        if (!IS_INIT) {
            synchronized (Collector.class) {
                if (!IS_INIT) {
                    initCommon();
                    initLogger();
                    initSdkMode();
                    initConsumer();
                    initHook();
                    initVerifySender();
                    IS_INIT = true;
                    logger.info("sdk config properties: \r\n{}", properties);
                    System.out.println("sdk config properties: \r\n" + properties);
                }
            }
        }
    }

    private void initVerifySender(){
        verifySender = new VerifySender(properties.getVerify());
    }

    private void initSdkMode() {
        initModeFile();
        initModeHttp();
        initModeKafka();
    }

    /**
     * 日志pool初始化
     */
    private void initLogger() {
        logger.info("init log writer pool");
        String eventSaveName = properties.getEventSaveName();
        int eventSaveMaxFileSize = properties.getEventSaveMaxFileSize();
        String eventSavePath = properties.getEventSavePath();
        //定时记录日志的条数
        scheduled = Executors.newSingleThreadScheduledExecutor();

        scheduled
                .scheduleAtFixedRate(new CollectorCounter(eventSavePath), 1, 2, TimeUnit.MINUTES);

        if (properties.getCallback() == null) {
            properties.setCallback(new LoggingCallback(eventSavePath, "error-" + eventSaveName,
                    eventSaveMaxFileSize));
        }
    }

    /**
     * eventConfig,httpclient,EventConfig 初始化
     */
    private void initCommon() {
        // 如果客户自定义了queue，则需要替换为客户自定义queue，否则使用默认的队列
        CollectorQueue userQueue = this.properties.getUserQueue();
        if (userQueue == null) {
            collectorContainer = new CollectorContainer(
                    RangersCollectorQueue.getInstance(this.properties.getQueueSize()));
        } else {
            collectorContainer = new CollectorContainer(userQueue);
        }
    }

    /**
     * 初始化消费者, httpRequestPool, 日志记录和清理任务
     */
    private void initConsumer() {
        logger.info("init consumer");
        boolean isSync = this.properties.isSync();

        // 同步设置
        if (isSync) {
            setConsumer(new Consumer(Collector.collectorContainer, this.properties));
            return;
        }

        // 异步起多个消费者
        if (executorService == null) {
            int threadCount = this.properties.getThreadCount();
            executorService = Executors.newFixedThreadPool(threadCount);

            // 创建consumer
            for (int i = 0; i < threadCount; i++) {
                //必须全部消费同一个队列
                executorService.execute(new Consumer(collectorContainer, properties));
            }
        }
    }

    private void initModeHttp() {
        if(SdkMode.HTTP != this.properties.getMode()){
            return;
        }
        HttpConfig httpConfig = properties.getHttpConfig();
        HttpClient httpClient = properties.getCustomHttpClient();
        Callback callback = properties.getCallback();
        int httpTimeOut = properties.getHttpTimeout();

        //EventConfig配置
        EventConfig.setUrl(properties.getDomain());
        if (httpConfig.getMaxPerRoute() < properties.getThreadCount()) {
            httpConfig.setMaxPerRoute(properties.getThreadCount());
        }
        if (httpConfig.getMaxTotal() < httpConfig.getMaxPerRoute()) {
            httpConfig.setMaxTotal(httpConfig.getMaxPerRoute());
        }
        // 老版本配置做兼容
        httpConfig.initTimeOut(httpTimeOut);
        //httpclient 初始化
        HttpUtils.createHttpClient(httpConfig, httpClient, callback);

        //EventConfig 初始化
        if (EventConfig.SEND_HEADER == null) {
            EventConfig.SEND_HEADER = properties.getHeaders();
            EventConfig.SEND_HEADER.put("User-Agent", "DataRangers Java SDK");
            EventConfig.SEND_HEADER.put("Content-Type", "application/json");
            List<Header> headerList = new ArrayList<>();
            EventConfig.SEND_HEADER
                    .forEach((key, value) -> headerList.add(new BasicHeader(key, value)));
            EventConfig.headers = headerList.toArray(new Header[0]);
        }
    }

    private void initModeFile() {
        if(SdkMode.FILE != this.properties.getMode()){
            return;
        }
        // thread 设置为1
        this.properties.setThreadCount(1);

        List<String> eventFilePaths = this.properties.getEventFilePaths();
        String eventSaveName = this.properties.getEventSaveName();
        int eventSaveMaxDays = this.properties.getEventSaveMaxDays();
        int eventSaveMaxFileSize = this.properties.getEventSaveMaxFileSize();

        Consumer.setWriterPool(eventFilePaths, eventSaveName, eventSaveMaxFileSize);
        if (eventSaveMaxDays > 0) {
            // 清理日志文件定时任务, 每隔12小时清理一次
            scheduled.scheduleAtFixedRate(
                    new RangersFileCleaner(eventFilePaths, eventSaveName, eventSaveMaxDays),
                    0, 12, TimeUnit.HOURS);
            logger.info("Start DataRangers Cleaner/Record Thread");
        }
    }

    /**
     * jvm关闭时，一些需要做的清理任务
     */
    private void initHook() {
        logger.info("init hook");
        Runtime.getRuntime().addShutdownHook(new Thread(
                () -> {
                    if (Collector.executorService != null) {
                        Collector.executorService.shutdown();
                    }

                    new Consumer(Collector.collectorContainer, properties).flush();
                }));
    }
}
