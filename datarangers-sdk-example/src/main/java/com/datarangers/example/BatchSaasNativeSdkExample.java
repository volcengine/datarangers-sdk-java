/*
 * Copyright 2020 Beijing Volcano Engine Technology Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
 * the specific language governing permissions and limitations under the License.
 */

package com.datarangers.example;

import com.datarangers.collector.AppEventCollector;
import com.datarangers.collector.EventCollector;
import com.datarangers.config.Constants;
import com.datarangers.config.DataRangersSDKConfigProperties;
import com.datarangers.config.SdkMode;

/**
 * HTTP模式的特殊场景，当跨网络传输，http 时延比较大的场景，可以使用批量上报的方式来提高性能
 *
 * @Author zhangpeng.spin@bytedance.com
 * @Date 2022/9/28
 */
public class BatchSaasNativeSdkExample extends AbstractSdkExample {
    private EventCollector appEventCollector;
    private EventCollector webEventCollector;
    private EventCollector mpEventCollector;

    BatchSaasNativeSdkExample() {
        DataRangersSDKConfigProperties properties = new DataRangersSDKConfigProperties();
        // 设置模式
        properties.setEnv(Constants.ENV_SAAS_NATIVE);
        properties.setMode(SdkMode.HTTP);

        // 设置domain和appKey
        properties.setDomain("https://gator.volces.com");

        // 可以设置多个app，这里注意替换成真实的参数
        properties.getAppKeys().put(Integer.valueOf(System.getenv("SDK_APP_1")), System.getenv("SDK_APP_KEY_1"));
        properties.getAppKeys().put(Integer.valueOf(System.getenv("SDK_APP_2")), System.getenv("SDK_APP_KEY_2"));

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
        BatchSaasNativeSdkExample sdkExample = new BatchSaasNativeSdkExample();
        String userUniqueId1 = "test_sdk_user_app111";
        String userUniqueId2 = "test_sdk_user_app222";

        int appId1 = Integer.valueOf(System.getenv("SDK_APP_1"));
        int appId2 = Integer.valueOf(System.getenv("SDK_APP_2"));

        for (int i = 0; i < 5; i++) {
            // 发送事件，时间发生时间为send方法调用的时间
            sdkExample.sendEvent(userUniqueId1, appId1);
            sdkExample.sendEvent(userUniqueId2, appId2);

            sdkExample.senEventWithAbSdk(userUniqueId1, appId1);
            sdkExample.senEventWithAbSdk(userUniqueId2, appId2);

            sdkExample.sendUserProfile(userUniqueId1, appId1);
            sdkExample.sendUserProfile(userUniqueId2, appId2);

            // 指定localTimeMs时间,即事件发生时间
            sdkExample.sendEventWithLocalTimeMs(userUniqueId1, appId1, System.currentTimeMillis());
            sdkExample.sendEventWithLocalTimeMs(userUniqueId2, appId2, System.currentTimeMillis());
        }

    }
}
