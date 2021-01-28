/*
 * Copyright 2020 Beijing Volcano Engine Technology Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.datarangers.asynccollector;

import com.datarangers.message.Message;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

public class CollectorContainer {
    private BlockingQueue<Message> blockingQueue;
    public static final ConcurrentHashMap<String, ConcurrentHashMap<String, LongAdder>> SEND_HISTORY = new ConcurrentHashMap<>();

    public CollectorContainer(BlockingQueue<Message> blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    public boolean produce(Message message) throws InterruptedException {
        return blockingQueue.offer(message);
    }

    private Message handleMessage(Message message) throws InterruptedException {
        if (message == null) return null;
        int appId = message.getAppId();
        message.getEvents().forEach(event -> {
            String eventName = event.getEvent();
            String date = event.getDatetime().substring(0, 13);
            if (!SEND_HISTORY.containsKey(date)) {
                SEND_HISTORY.put(date, new ConcurrentHashMap<>());
            }
            String key = appId + "-" + eventName;
            ConcurrentHashMap<String, LongAdder> map = SEND_HISTORY.get(date);
            if (!map.containsKey(key)) map.put(key, new LongAdder());
            map.get(key).increment();
        });

        return message;
    }

    public Message consume() throws InterruptedException {
        return handleMessage(blockingQueue.take());
    }

    public Message consume(int waitTimeMs) throws InterruptedException {
        return handleMessage(blockingQueue.poll(waitTimeMs, TimeUnit.MILLISECONDS));
    }
}
