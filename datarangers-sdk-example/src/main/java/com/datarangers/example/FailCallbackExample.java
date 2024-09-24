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
import com.datarangers.sender.Callback;

/**
 * 适用于需要自定义发送失败后处理的场景
 *
 * @Author zhangpeng.spin@bytedance.com
 * @Date 2022/9/29
 */
public abstract class FailCallbackExample extends AbstractSdkExample {
    private EventCollector appEventCollector;

    FailCallbackExample() {
        DataRangersSDKConfigProperties properties = new DataRangersSDKConfigProperties();
        // 设置模式
        properties.setMode(SdkMode.HTTP);

        // 设置sdk domain，这里注意替换成真实的参数
        properties.setDomain(System.getenv("SDK_DOMAIN"));

        // 自定义失败处理方式
        Callback callback = failedData -> {
            System.out.println("message: " + failedData.getMessage());
            System.out.println("cause: " + failedData.getCause());
            System.out.println("exception: " + failedData.getException());
        };

        // 初始化collector
        appEventCollector = new AppEventCollector("app", properties, callback);
    }

    @Override
    public EventCollector getAppEventCollector() {
        return appEventCollector;
    }
}
