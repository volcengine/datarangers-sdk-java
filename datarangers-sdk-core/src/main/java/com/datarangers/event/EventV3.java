/*
 * Copyright 2020 Beijing Volcano Engine Technology Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.datarangers.event;

import com.alibaba.fastjson.annotation.JSONField;
import com.datarangers.config.Constants;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventV3 implements Event, Serializable {
    private String event;

    private Map<String, Object> params = new HashMap<>();

    private String sessionId;

    private Long localTimeMs = System.currentTimeMillis();

    @JSONField(name = "datetime")
    private String datetime = LocalDateTime.now().format(Constants.FULL_DAY);

    private Integer eventId;

    @JSONField(serialize = false)
    private String userId;

    private Long teaEventIndex;

    @JSONField(serialize = false)
    private Map<String, List<Object>> itemParams = new HashMap<>();

    public String getEvent() {
        return event;
    }

    @Override
    public EventV3 setEvent(String event) {
        this.event = event;
        return this;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    @Override
    public EventV3 setParams(Map<String, Object> params) {
        if (params != null) {
            params.forEach((key, value) -> {
                if (value instanceof Items) {
                    Map<String, Object> itemMap = new HashMap<>();
                    itemMap.put("id", ((Items) value).getId());
                    String name = ((Items) value).itemName;
                    List<Object> list = itemParams.getOrDefault(name, new ArrayList<>());
                    list.add(itemMap);
                    itemParams.put(name, list);
                } else {
                    this.params.put(key, value);
                }
            });
            List<Object> itemP = new ArrayList<>();
            if (itemParams.size() > 0) {
                itemParams.forEach((key, value) -> {
                    Map<String, List<Object>> res = new HashMap<>();
                    res.put(key, value);
                    itemP.add(res);
                });
            }
            this.params.put("__items", itemP);
        }
        return this;
    }

    @Override
    public Event addParams(String key, Object value) {
        this.params.put(key, value);
        return this;
    }

    public String getSessionId() {
        return sessionId;
    }

    public EventV3 setSessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public Long getLocalTimeMs() {
        return localTimeMs;
    }

    public EventV3 setLocalTimeMs(Long localTimeMs) {
        this.localTimeMs = localTimeMs;
        return this;
    }

    public String getDatetime() {
        return datetime;
    }

    public EventV3 setDatetime(String datetime) {
        this.datetime = datetime;
        return this;
    }

    public Integer getEventId() {
        return eventId;
    }

    public EventV3 setEventId(Integer eventId) {
        this.eventId = eventId;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public EventV3 setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public Long getTeaEventIndex() {
        return teaEventIndex;
    }

    public EventV3 setTeaEventIndex(Long teaEventIndex) {
        this.teaEventIndex = teaEventIndex;
        return this;
    }
}
