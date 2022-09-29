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
import com.datarangers.config.SdkMode;

/**
 * @Author zhangpeng.spin@bytedance.com
 * @Date 2022/9/28
 */
public class BatchPriSdkExample extends AbstractSdkExample {
    private EventCollector appEventCollector;
    private EventCollector webEventCollector;
    private EventCollector mpEventCollector;

    BatchPriSdkExample() {
        DataRangersSDKConfigProperties properties = new DataRangersSDKConfigProperties();
        // 设置模式
        properties.setMode(SdkMode.HTTP);

        // 设置domain和host
        properties.setDomain(System.getenv("SDK_DOMAIN"));
        properties.getHeaders().put("HOST", System.getenv("SDK_HOST"));

        // 设置batch
        properties.setSendBatch(true);

        // 可以根据需要进行调试batchSize,一般使用默认的即可
        // properties.setBatchSize(20);
        // properties.setWaitTimeMs(100);

        // 初始化collector
        appEventCollector = new AppEventCollector("app", properties);
        webEventCollector = new AppEventCollector("web", properties);
        mpEventCollector = new AppEventCollector("mp", properties);
    }


    @Override
    public EventCollector getAppEventCollector() {
        return appEventCollector;
    }

    @Override
    public EventCollector getWebEventCollector() {
        return webEventCollector;
    }

    @Override
    public EventCollector getMpEventCollector() {
        return mpEventCollector;
    }

    public static void main(String[] args) {
        BatchPriSdkExample sdkExample = new BatchPriSdkExample();
        String userUniqueId = "test_sdk_user1";
        int appId = 10000000;

        // 发送事件，时间发生时间为send方法调用的时间
        sdkExample.sendEvent(userUniqueId, appId);
        sdkExample.senEventWithAbSdk(userUniqueId, appId);
        sdkExample.sendUserProfile(userUniqueId, appId);

        // 指定localTimeMs时间,即事件发生时间
        long localTimeMs = System.currentTimeMillis();
        sdkExample.sendEventWithLocalTimeMs(userUniqueId, appId, localTimeMs);
    }
}
