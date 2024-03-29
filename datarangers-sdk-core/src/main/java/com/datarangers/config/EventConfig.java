/*
 * Copyright 2020 Beijing Volcano Engine Technology Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.datarangers.config;

import org.apache.hc.core5.http.Header;

import java.util.Map;

public class EventConfig {
    public static String appUrl;
    public static String appListUrl;
    public static Header[] headers;
    public static Map<String, String> SEND_HEADER;

    public static void setUrl(String url) {
        setAppUrl(url + Constants.APP_LOG_PATH);
        setAppListUrl(url + Constants.APP_LIST_PATH);
    }

    public static String getAppUrl() {
        return EventConfig.appUrl;
    }

    public static void setAppUrl(String appUrl) {
        EventConfig.appUrl = appUrl;
    }

    public static String getAppListUrl() {
        return EventConfig.appListUrl;
    }

    public static void setAppListUrl(String appUrl) {
        EventConfig.appListUrl = appUrl;
    }
}
