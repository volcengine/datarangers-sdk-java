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
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("${PROFILE}")
class SdkExampleTest extends AbstractSdkExample {

    @Autowired
    @Qualifier("appEventCollector")
    private AppEventCollector appEventCollector;

    @Autowired
    @Qualifier("webEventCollector")
    private AppEventCollector webEventCollector;

    @Autowired
    @Qualifier("mpEventCollector")
    private AppEventCollector mpEventCollector;

    private int appId;
    private String userUniqueId;

    @BeforeEach
    public void beforeEach() {
        appId = 10000000;
        userUniqueId = "test_sdk_user2";
    }

    @AfterEach
    public void afterEach() {
        // 优雅退出
        try {
            Thread.sleep(10 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    public static void clearUp() {
        try {
            Thread.sleep(10 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void sendEvent() {
        sendEvent(userUniqueId, appId);
    }

    @Test
    public void senEventWithAbSdk() {
        senEventWithAbSdk(userUniqueId, appId);
    }

    @Test
    public void sendUserProfile() {
        sendUserProfile(userUniqueId, appId);
    }

    @Test
    public void sendItemProfile() {
        sendEventWithItem(userUniqueId, appId);
    }

    @Test
    public void sendEventWithItem() {
        sendEventWithItem(userUniqueId, appId);
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

}
