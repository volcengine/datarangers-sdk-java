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
import com.datarangers.profile.ProfileItem;

import java.util.List;

@Deprecated
public class ProfileMessage implements Message {
    @Override
    public void merge() {
    }

    @Override
    public void setUserUniqueId(String uuid) {
    }

    @Override
    public String getUserUniqueId() {
        return "";
    }

    @Override
    public void setAppType(String appType) {
    }

    @Override
    public void setHeader(Header header) {
    }

    @Override
    public void addEvent(Event event) {
    }

    @Override
    public void addEvents(List<Event> events) {
    }

    private List<ProfileItem> items;
    @JSONField(name = "app_type")
    private String appType = "profile";

    private Integer appId;
    @JSONField(serialize = false)
    private String url;

    public List<ProfileItem> getItems() {
        return items;
    }

    public ProfileMessage setItems(List<ProfileItem> items) {
        this.items = items;
        if (items.size() > 0)
            setAppId(items.get(0).getAppId());
        else
            setAppId(0);
        return this;
    }

    public Integer getAppId() {
        return appId;
    }

    public ProfileMessage setAppId(Integer appId) {
        this.appId = appId;
        return this;
    }

    @Override
    public String getUrl() {
        return EventConfig.getAppUrl();
    }

    public String getAppType() {
        return appType;
    }
}
