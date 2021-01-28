/*
 * Copyright 2020 Beijing Volcano Engine Technology Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.datarangers.asynccollector;

import com.alibaba.fastjson.JSONObject;
import com.datarangers.config.EventConfig;
import com.datarangers.logger.RangersLoggerWriter;
import com.datarangers.logger.RangersLoggerWriterPool;
import com.datarangers.message.Message;
import com.datarangers.util.HttpUtils;

import java.util.ArrayList;
import java.util.List;

public class Consumer implements Runnable {
    private static RangersLoggerWriterPool pool;
    private CollectorContainer collectorContainer;

    public Consumer(CollectorContainer collectorContainer) {
        this.collectorContainer = collectorContainer;
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

    private List<Message> getMessages(int count) throws InterruptedException {
        List<Message> messages = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Message message = collectorContainer.consume(50);
            if (message != null)
                messages.add(message);
        }
        return messages.size() == 0 ? null : messages;
    }

    private void send() throws Exception {
        while (true) {
            try {
                Message message = collectorContainer.consume();
                if (message != null)
                    HttpUtils.post(EventConfig.getAppUrl(), JSONObject.toJSONString(message, EventConfig.config), EventConfig.SEND_HEADER);
            }catch (InterruptedException e){

            }

        }
    }

    private void write() throws Exception {
        while (true) {
            Message message = collectorContainer.consume();
            if (message != null) {
                pool.getWriter(message.getUserUniqueId()).write(JSONObject.toJSONString(message, EventConfig.config) + "\n");
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
