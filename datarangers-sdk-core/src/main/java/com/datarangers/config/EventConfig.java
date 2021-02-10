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
    public static Header[] headers;
    public static String profileUrl;
    public static String mpUrl;
    public static Map<String, String> SEND_HEADER;
    public static boolean saveFlag = false;
    public static boolean sendFlag = true;
    public static String ssidInfoUrl;
    public static boolean batchMode=false;

    public static void setUrl(String url) {
        setAppUrl(url + Constants.APP_LOG_URL);
        setMpUrl(url + Constants.MP_URL);
        setProfileUrl(url + Constants.PROFILE_URL);
        setSsidInfoUrl(url + Constants.SSID_INFO_URL);
    }

    public static String getSsidInfoUrl() {
        return EventConfig.ssidInfoUrl;
    }

    public static void setSsidInfoUrl(String ssidInfoUrl) {
        EventConfig.ssidInfoUrl = ssidInfoUrl;
    }

    public static String getAppUrl() {
        return EventConfig.appUrl;
    }

    public static void setAppUrl(String appUrl) {
        EventConfig.appUrl = appUrl;
    }

    public static String getMpUrl() {
        return EventConfig.mpUrl;
    }

    public static void setMpUrl(String mpUrl) {
        EventConfig.mpUrl = mpUrl;
    }


    public static void setProfileUrl(String profileUrl) {
        EventConfig.profileUrl = profileUrl;
    }

    public static String getProfileUrl() {
        return profileUrl;
    }

}
