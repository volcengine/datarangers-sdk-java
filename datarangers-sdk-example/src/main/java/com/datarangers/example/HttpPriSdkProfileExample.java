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

import com.datarangers.collector.EventCollector;
import com.datarangers.event.Header;
import com.datarangers.event.HeaderV3;

import java.util.HashMap;

/**
 * 在私有化场景，直接使用 Http 模式使用进行发送事件
 *
 * @Author zhangpeng.spin@bytedance.com
 * @Date 2022/9/28
 */
public class HttpPriSdkProfileExample extends HttpPriSdkExample {

    HttpPriSdkProfileExample() {
        super();
    }

    public void sendUserProfileWithHeader(Header header) {
        EventCollector appEventCollector = getAppEventCollector();
        appEventCollector.profileSet(header, new HashMap<String, Object>() {{
            put("profile_a", "param_11");
            put("profile_b", "param_22");
            put("profile_c", "param_33");
            put("profile_d", "param_55");
        }});
    }

    public static void main(String[] args) {
        HttpPriSdkProfileExample sdkExample = new HttpPriSdkProfileExample();
        String userUniqueId = "test_sdk_user1";
        Long deviceId = 123456L;
        int appId = 10000001;

        // 上报用户属性
        Header header = new HeaderV3.Builder()
                .setUserUniqueId(userUniqueId)
                .setDeviceId(deviceId)
                .setAppId(appId)
                .build();

        sdkExample.sendUserProfileWithHeader(header);

        // 指定localTimeMs时间,即事件发生时间
        long localTimeMs = System.currentTimeMillis();
        sdkExample.sendEventWithLocalTimeMs(userUniqueId, appId, localTimeMs);
    }

}