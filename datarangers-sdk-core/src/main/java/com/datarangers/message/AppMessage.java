/*
 * Copyright 2020 Beijing Volcano Engine Technology Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.datarangers.message;

import com.alibaba.fastjson.annotation.JSONField;
import com.datarangers.config.EventConfig;
import com.datarangers.event.Event;
import com.datarangers.event.Header;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppMessage implements Message {
    @JSONField(name = "app_type")
    private String appType;

    @JSONField(name = "_format_name")
    private String formatName = "datarangers_svc_app_log_v3_server_sdk_1.2.7";

    @JSONField(name = "client_ip")
    private String clientIp;

    @JSONField(name = "trace_id")
    private String traceId;

    @JSONField(name = "app_id")
    private int appId;

    @JSONField(name = "header")
    private Header header;

    @JSONField(name = "user_unique_id")
    private String userUniqueId;

    @JSONField(name = "time_sync", serialize = false)
    private Map<String, Object> timeSync = new HashMap<String, Object>() {{
        put("local_time", System.currentTimeMillis() / 1000);
    }};

    @JSONField(name = "event_v3")
    private List<Event> events = new ArrayList<>();

    @JSONField(serialize = false)
    private String url;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
        setAppId(header.getAppId());
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public void addEvents(List<Event> events) {
        this.events.addAll(events);
    }

    @Override
    public String getUrl() {
        return EventConfig.getAppUrl();
    }

    public void addEvent(Event event) {
        events.add(event);
    }

    public void merge() {
        if (header.getDeviceId() < 0) {
            header.setDeviceId(1l);
        }
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getFormatName() {
        return formatName;
    }

    public void setFormatName(String formatName) {
        this.formatName = formatName;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getUserUniqueId() {
        return userUniqueId;
    }

    public void setUserUniqueId(String userUniqueId) {
        this.userUniqueId = userUniqueId;
    }

    public Map<String, Object> getTimeSync() {
        return timeSync;
    }

    public void setTimeSync(Map<String, Object> timeSync) {
        this.timeSync = timeSync;
    }
}
