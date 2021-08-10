/*
 * Copyright 2020 Beijing Volcano Engine Technology Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.datarangers.asynccollector;

import com.datarangers.config.DataRangersSDKConfigProperties;
import com.datarangers.config.EventConfig;
import com.datarangers.config.RangersJSONConfig;
import com.datarangers.logger.RangersLoggerWriterPool;
import com.datarangers.message.AppMessage;
import com.datarangers.message.Message;
import com.datarangers.sender.MessageSenderFactory;

import java.util.List;

public class Consumer implements Runnable {
    private static RangersLoggerWriterPool pool;
    private CollectorContainer collectorContainer;
    private DataRangersSDKConfigProperties sdkConfigProperties;

    public Consumer(CollectorContainer collectorContainer, DataRangersSDKConfigProperties sdkConfigProperties) {
        this.collectorContainer = collectorContainer;
        this.sdkConfigProperties = sdkConfigProperties;
    }

    public static void setWriterPool(final List<String> targetPrefixes, String targetNames, int maxSize) {
        if (pool == null) {
            synchronized (Consumer.class) {
                if (pool == null) {
                    pool = RangersLoggerWriterPool.getInstance(targetPrefixes, targetNames, maxSize);
                }
            }
        }
    }

    private void send() throws Exception {
        while (true) {
            try {
                List<Message> messages = collectorContainer.consume();
                if (messages != null) {
                    for(Message message:messages){
                        MessageSenderFactory.getMessageSender(message).senderMessage(message, this.sdkConfigProperties);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void write() throws Exception {
        while (true) {
            List<Message> messages = collectorContainer.consume();
            if (messages != null) {
                for(Message message:messages){
                    AppMessage appMessage = message.getAppMessage();
                    pool.getWriter(appMessage.getUserUniqueId()).write(RangersJSONConfig.getInstance().toJson(appMessage) + "\n");
                }
            }
        }
    }

    @Override
    public void run() {
        try {
            if (EventConfig.saveFlag) write();
            else send();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
