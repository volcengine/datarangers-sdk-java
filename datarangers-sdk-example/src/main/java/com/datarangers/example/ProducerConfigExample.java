/*
 * Copyright 2020 Beijing Volcano Engine Technology Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package com.datarangers.example;

import com.datarangers.collector.AppEventCollector;
import com.datarangers.collector.EventCollector;
import com.datarangers.config.DataRangersSDKConfigProperties;
import com.datarangers.config.ProducerConfig;
import com.datarangers.config.SdkMode;
import com.datarangers.sender.Callback;

/**
 * 适用于需要自定义发送失败后处理的场景
 *
 * @Author zhangpeng.spin@bytedance.com
 * @Date 2022/9/29
 */
public class ProducerConfigExample extends AbstractSdkExample {
    private EventCollector appEventCollector;
    private EventCollector webEventCollector;
    private EventCollector mpEventCollector;

    @Override
    public EventCollector getAppEventCollector() {
        return appEventCollector;
    }

    @Override
    public EventCollector getWebEventCollector() {
        return null;
    }

    @Override
    public EventCollector getMpEventCollector() {
        return null;
    }

    ProducerConfigExample() {
        DataRangersSDKConfigProperties properties = new DataRangersSDKConfigProperties();
        // 设置模式
        properties.setMode(SdkMode.HTTP);

        // 设置队列大小为1
        properties.setQueueSize(10);

        // 设置domain和host，这里注意替换成真实的参数
        properties.setDomain(System.getenv("SDK_DOMAIN"));
        properties.getHeaders().put("HOST", System.getenv("SDK_HOST"));

        ProducerConfig producerConfig = new ProducerConfig();
        producerConfig.setUseOffer(true);
        producerConfig.setOfferTimeout(100);

        // 初始化collector
        webEventCollector = new AppEventCollector("web", properties, null, producerConfig);
        appEventCollector = new AppEventCollector("app", properties, null, producerConfig);
        mpEventCollector = new AppEventCollector("mp", properties, null, producerConfig);
    }

    public static void main(String[] args) {
        ProducerConfigExample sdkExample = new ProducerConfigExample();
        String userUniqueId = "test_sdk_user1";
        int appId = 10000000;

        // 发送事件，时间发生时间为send方法调用的时间
        for (int i = 0; i < 50; i++) {
            sdkExample.sendEvent(userUniqueId, appId);

            sdkExample.senEventPresetCommonParams(userUniqueId, appId);
        }
    }
}
