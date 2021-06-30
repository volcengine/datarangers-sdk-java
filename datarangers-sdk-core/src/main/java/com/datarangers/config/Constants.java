/*
 * Copyright 2020 Beijing Volcano Engine Technology Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.datarangers.config;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

public class Constants {
    public static final String SDK_VERSION = "datarangers_sdk_1.3.5-release";
    public static DateTimeFormatter FULL_HOUR = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH");
    public static DateTimeFormatter FULL_DAY = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final String APP_LOG_URL = "/sdk/log";
    public static final String DEFAULT_USER = "__rangers";

    public static final String INIT_ERROR = "sdk config must not be null";
    public static final int TIME_ZONE_OFFSET_CURRENT = new GregorianCalendar().getTimeZone().getOffset(System.currentTimeMillis()) / 1000;
    public static final ZoneId TIME_ZONE_ID=ZoneId.systemDefault();
    public static final String TIME_ZONE_NAME = TIME_ZONE_ID.getId();
    public static final int TIME_ZONE_OFFSET = TIME_ZONE_OFFSET_CURRENT / 60 / 60;
}
