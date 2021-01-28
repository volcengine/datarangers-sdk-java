/*
 * Copyright 2020 Beijing Volcano Engine Technology Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.datarangers.collector;

import com.alibaba.fastjson.JSON;
import com.datarangers.asynccollector.CollectorContainer;
import com.datarangers.config.EventConfig;
import com.datarangers.message.Message;
import com.datarangers.message.ProfileMessage;
import com.datarangers.profile.ProfileItem;
import com.datarangers.profile.ProfileRequestType;
import com.datarangers.util.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;

public abstract class Collector implements EventCollector {
    private String appType;
    public static final Logger logger = LoggerFactory.getLogger("DatarangersLog");
    public static Executor httpRequestPool = null;
    public static ScheduledExecutorService scheduled = null;
    public static BlockingQueue<Message> blockingQueue;
    public static CollectorContainer collectorContainer;

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
        message.merge();
        String sendMessage = JSON.toJSONString(message, EventConfig.config);
        if (EventConfig.sendFlag) {
            if (blockingQueue != null) {
                try {
                    if (!collectorContainer.produce(message)) {
                        //队列满了,需要保存到log
                        logger.error("datarangers send Queue reach max length");
                        HttpUtils.writeFailedMessage(sendMessage);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                HttpUtils.post(message.getUrl(), sendMessage, EventConfig.SEND_HEADER);
            }
        }
    }
}
