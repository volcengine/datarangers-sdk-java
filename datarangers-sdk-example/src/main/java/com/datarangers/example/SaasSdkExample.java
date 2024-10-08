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
 * saas 使用场景
 *
 * @Author zhangpeng.spin@bytedance.com
 * @Date 2022/9/28
 */
public class SaasSdkExample extends AbstractSdkExample {
    private EventCollector appEventCollector;
    private EventCollector webEventCollector;
    private EventCollector mpEventCollector;

    SaasSdkExample() {
        DataRangersSDKConfigProperties properties = new DataRangersSDKConfigProperties();
        // 设置模式
        properties.setMode(SdkMode.HTTP);

        // 设置domain和appKey
        properties.setDomain("https://mcs.ctobsnssdk.com");

        // 可以设置多个app，这里注意替换成真实的参数
        properties.getAppKeys().put(Integer.valueOf(System.getenv("SDK_APP_1")), System.getenv("SDK_APP_KEY_1"));

        // 设置openapi domain, AK,SK，这里注意替换成真实的参数
        properties.getOpenapiConfig().setDomain("https://analytics.volcengineapi.com");
        properties.getOpenapiConfig().setAk(System.getenv("OPENAPI_AK"));
        properties.getOpenapiConfig().setSk(System.getenv("OPENAPI_SK"));

        // 设置服务端埋点实时校验url
        // properties.getVerify().setUrl(System.getenv("VERIFY_URL"));


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
        SaasSdkExample sdkExample = new SaasSdkExample();
        String userUniqueId = "test_sdk_user1";
        int appId = Integer.valueOf(System.getenv("SDK_APP_1"));
        sdkExample.sendEventWithHeader(userUniqueId, appId);

        try {
            Thread.sleep(10* 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        sdkExample.sendEventWithHeader(userUniqueId, appId);

        // 发送事件，时间发生时间为send方法调用的时间
        sdkExample.sendEvent(userUniqueId, appId);

        sdkExample.senEventPresetCommonParams(userUniqueId, appId);

        // 上报用户属性，需要保证先在系统新增用户属性
        sdkExample.sendUserProfile(userUniqueId, appId);

        // 指定localTimeMs时间,即事件发生时间
        long localTimeMs = System.currentTimeMillis();
        sdkExample.sendEventWithLocalTimeMs(userUniqueId, appId, localTimeMs);
    }
}
