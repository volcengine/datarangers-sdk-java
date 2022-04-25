/*
 * Copyright 2020 Beijing Volcano Engine Technology Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.datarangers.asynccollector;

import com.datarangers.message.Message;

import java.util.List;

/**
 * @Description: SDK使用的队列
 * @Author: hezhiwei.alden@bytedance.com
 * @Date 2021/2/4 14:48
 **/
public interface CollectorQueue {
    /**
     * 功能描述: 从队列里消费一批message，没有过期时间，如果自行设置过期时间则需要在没有数据的时候返回null
     *
     * @return: java.util.List<com.datarangers.message.Message>
     * @date: 2021/2/7 15:51
     */
    List<Message> take() throws InterruptedException;
    List<Message> poll(int waitTimeMs) throws InterruptedException;
    /**
     * 功能描述: 发送一个Message到队列中
     *
     * @param: [t]
     * @return: boolean 是否发送成功
     * @date: 2021/2/7 15:52
     */
    void put(Message t)throws InterruptedException;
    int size();

    /**
     * 遍历队列
     * @return
     */
    Message poll();
}
