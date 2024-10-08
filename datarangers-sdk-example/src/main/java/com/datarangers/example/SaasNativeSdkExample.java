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
import com.datarangers.config.Constants;
import com.datarangers.config.DataRangersSDKConfigProperties;
import com.datarangers.config.SdkMode;

/**
 * saas 使用场景
 *
 * @Author zhangpeng.spin@bytedance.com
 * @Date 2022/9/28
 */
public class SaasNativeSdkExample extends AbstractSdkExample {
    private EventCollector appEventCollector;
    private EventCollector webEventCollector;
    private EventCollector mpEventCollector;

    SaasNativeSdkExample() {
        DataRangersSDKConfigProperties properties = new DataRangersSDKConfigProperties();
        // 设置模式
        properties.setEnv(Constants.ENV_SAAS_NATIVE);
        properties.setMode(SdkMode.HTTP);

        // 设置domain和appKey
        properties.setDomain("https://gator.volces.com");

        // 可以设置多个app，这里注意替换成真实的参数
        properties.getAppKeys().put(Integer.valueOf(System.getenv("SDK_APP_1")), System.getenv("SDK_APP_KEY_1"));

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
        SaasNativeSdkExample sdkExample = new SaasNativeSdkExample();
        String userUniqueId = "test_sdk_user1";
        int appId = Integer.valueOf(System.getenv("SDK_APP_1"));

        // 发送事件，时间发生时间为send方法调用的时间
        sdkExample.sendEvent(userUniqueId, appId);

        // 上报用户属性
        sdkExample.sendUserProfile(userUniqueId, appId);

        // 指定localTimeMs时间,即事件发生时间
        long localTimeMs = System.currentTimeMillis();
        sdkExample.sendEventWithLocalTimeMs(userUniqueId, appId, localTimeMs);
    }
}
