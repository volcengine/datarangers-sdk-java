/*
 * Copyright 2020 Beijing Volcano Engine Technology Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.datarangers.asynccollector;

import com.datarangers.message.Message;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author hTangle
 */
public class CollectorContainer {
    private CollectorQueue messageQueue;
    public static final ConcurrentHashMap<String, ConcurrentHashMap<String, LongAdder>> SEND_HISTORY = new ConcurrentHashMap<>();

    public CollectorContainer(CollectorQueue messageQueue) {
        this.messageQueue = messageQueue;
    }

    public void produce(Message message) throws InterruptedException {
        messageQueue.put(message);
    }

    private List<Message> handleMessage(List<Message> messages) throws InterruptedException {
        if (messages == null) {
            return null;
        }
        messages.forEach(message -> doHandleMessage(message));
        return messages;
    }

    public List<Message> consume() throws InterruptedException {
        return handleMessage(messageQueue.take());
    }

    public List<Message> consume(int waitTimeMs) throws InterruptedException {
        return handleMessage(messageQueue.poll(waitTimeMs));
    }

    public List<Message> consume(int size, int waitTimeMs) throws InterruptedException {
        return handleMessage(messageQueue.poll(size, waitTimeMs));
    }

    public int size() {
        return messageQueue.size();
    }

    public CollectorQueue getMessageQueue() {
        return messageQueue;
    }

    public Message handleMessage(Message message) {
        if (message == null) {
            return null;
        }
        doHandleMessage(message);
        return message;
    }

    private void doHandleMessage(Message message) {
        message.getAppMessage().getEvents().forEach(event -> {
            int appId = message.getAppMessage().getAppId();
            String eventName = event.getEvent();
            String date = event.getDatetime().substring(0, 13);
            if (!SEND_HISTORY.containsKey(date)) {
                SEND_HISTORY.put(date, new ConcurrentHashMap<>());
            }
            String key = appId + "-" + eventName;
            ConcurrentHashMap<String, LongAdder> map = SEND_HISTORY.get(date);
            if (!map.containsKey(key)) {
                map.put(key, new LongAdder());
            }
            map.get(key).increment();
        });
    }


}
