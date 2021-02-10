/*
 * Copyright 2020 Beijing Volcano Engine Technology Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.datarangers.message;

import com.datarangers.config.EventConfig;
import com.datarangers.event.Event;
import com.datarangers.event.Header;
import com.datarangers.event.HeaderV3;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class AppMessage implements Message {
    @JsonProperty("app_type")
    private String appType;

    @JsonProperty("_format_name")
    private String formatName = "datarangers_svc_app_log_v3_server_sdk_1.2.7";

    @JsonProperty("client_ip")
    private String clientIp;

    @JsonProperty("trace_id")
    private String traceId;

    @JsonProperty("app_id")
    private int appId;

    @JsonProperty("header")
    private HeaderV3 header;

    @JsonProperty("user_unique_id")
    private String userUniqueId;

    @JsonProperty("event_v3")
    private List<Event> events = new ArrayList<>();

    @JsonIgnore
    private String url;

    public HeaderV3 getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = (HeaderV3)header;
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
}
