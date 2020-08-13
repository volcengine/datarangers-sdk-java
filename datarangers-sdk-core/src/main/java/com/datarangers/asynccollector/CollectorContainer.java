/*
 * Copyright 2020 Beijing Volcano Engine Technology Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
*/
package com.datarangers.asynccollector;

import com.datarangers.message.Message;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class CollectorContainer {
    private BlockingQueue<Message> blockingQueue;

    public CollectorContainer(BlockingQueue<Message> blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    public boolean produce(Message message) throws InterruptedException {
        return blockingQueue.offer(message);
    }

    public Message consume() throws InterruptedException {
        return blockingQueue.take();
    }

    public Message consume(int waitTime) throws InterruptedException {
        return blockingQueue.poll(waitTime, TimeUnit.MILLISECONDS);
    }


}
