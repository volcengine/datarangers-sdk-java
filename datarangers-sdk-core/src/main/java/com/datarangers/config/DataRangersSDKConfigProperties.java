/*
 * Copyright 2020 Beijing Volcano Engine Technology Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.datarangers.config;

import com.alibaba.fastjson.PropertyNamingStrategy;
import com.datarangers.asynccollector.CollectorContainer;
import com.datarangers.asynccollector.CollectorCounter;
import com.datarangers.asynccollector.Consumer;
import com.datarangers.collector.Collector;
import com.datarangers.logger.RangersFileCleaner;
import com.datarangers.util.HttpUtils;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class DataRangersSDKConfigProperties {
    public static final Logger logger = LoggerFactory.getLogger(DataRangersSDKConfigProperties.class);
    public Map<String, String> headers;
    public String domain;
    public int threadPoolCount = 1;
    public int maxPoolSize = 8;
    public int corePoolSize = 4;
    public boolean async = true;
    public int httpTimeout = 500;
    public String timeZone = "+8";
    public ZoneOffset timeOffset = null;
    public boolean save = false;
    public int threadCount = 1;
    public int queueSize = 10240;
    public boolean send = true;

    public String eventSavePath = "logs/";
    public List<String> eventFilePaths;
    public String eventSaveName = "datarangers.log";
    public int eventSaveMaxHistory = 20;
    public int eventSaveMaxFileSize = 100;
    public int eventSaveMaxDays = 5;


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

    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    public Map<String, String> getHeaders() {
        if (headers == null) headers = new HashMap<>();
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

    public boolean isSave() {
        return save;
    }

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
        HttpUtils.setWriter(getEventSavePath(), "error-" + getEventSaveName(), getEventSaveMaxFileSize());
    }

    public void setCommon() {
        HttpUtils.setRequestTimeOut(getHttpTimeout());
        HttpUtils.createHttpClient();
        EventConfig.saveFlag = save;
        EventConfig.sendFlag = isSend();
        EventConfig.config.setPropertyNamingStrategy(PropertyNamingStrategy.SnakeCase);
        EventConfig.async = isAsync();
        if (EventConfig.SEND_HEADER == null) {
            EventConfig.SEND_HEADER = getHeaders();
            EventConfig.SEND_HEADER.put("User-Agent", "DataRangers Java SDK/1.2.0");
            EventConfig.SEND_HEADER.put("Content-Type", "application/json");
            List<Header> headerList = new ArrayList<>();
            EventConfig.SEND_HEADER.forEach((key, value) -> {
                headerList.add(new BasicHeader(key, value));
            });
            EventConfig.headers = headerList.toArray(new Header[0]);
        }
        if (EventConfig.SEND_HEADER.containsKey("Host")) {
            EventConfig.HOST = EventConfig.SEND_HEADER.get("Host");
        }
        if (isSend() && isAsync() && Collector.httpRequestPool == null) {
            Collector.httpRequestPool = setThreadPool();
            Collector.blockingQueue = new LinkedBlockingQueue<>(getQueueSize());
            Collector.collectorContainer = new CollectorContainer(Collector.blockingQueue);
            setConsumer(getThreadCount());
        }
        EventConfig.setUrl(getDomain());

    }

    public Executor setThreadPool() {
        return Executors.newFixedThreadPool(getCorePoolSize());
    }

    public void setConsumer(int threadCount) {
        if (EventConfig.saveFlag) {
            threadCount = 1;
            logger.info("Start LogAgent Mode");
        } else {
            logger.info("Start Http Mode");
        }

        for (int i = 0; i < threadCount; i++) {
            Collector.httpRequestPool.execute(new Consumer(new CollectorContainer(Collector.blockingQueue)));
        }
        Collector.scheduled = Executors.newSingleThreadScheduledExecutor();
        Collector.scheduled.scheduleAtFixedRate(new CollectorCounter(getEventSavePath()), 0, 2, TimeUnit.MINUTES);
        if (EventConfig.saveFlag) {
            Collector.scheduled.scheduleAtFixedRate(new RangersFileCleaner(getEventFilePaths(), getEventSaveName(), getEventSaveMaxDays()), 0, 12, TimeUnit.HOURS);
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
                    IS_INIT = true;
                }
            }
        }
    }
}

