/*
 * Copyright 2020 Beijing Volcano Engine Technology Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.datarangers.profile;

import com.alibaba.fastjson.annotation.JSONField;
import com.datarangers.config.Constants;

import java.util.HashMap;
import java.util.Map;

@Deprecated
public class ProfileItem {
    @JSONField(name = "app_id")
    private int appId;
    @JSONField(name = "request_type")
    private String requestType = "Set";
    @JSONField(name = "user_uniq_id")
    private String userUniqueId;
    private String property;
    private String type;
    private Map<String, Object> value = new HashMap<>();

    public static ProfileItem getInstance() {
        return new ProfileItem();
    }

    public ProfileItem() {
    }

    public ProfileItem(String userUniqueId, int appId, ProfileRequestType profileRequestType, String property, Object value) {
        setUserUniqueId(userUniqueId);
        setAppId(appId);
        setRequestType(profileRequestType.toString());
        setProperty(property);
        addValues(value);
    }

    public int getAppId() {
        return appId;
    }

    public ProfileItem setAppId(int appId) {
        this.appId = appId;
        return this;
    }

    public String getRequestType() {
        return requestType;
    }

    public ProfileItem setRequestType(String requestType) {
        this.requestType = requestType;
        return this;
    }

    public String getUserUniqueId() {
        return userUniqueId;
    }

    public ProfileItem setUserUniqueId(String userUniqueId) {
        this.userUniqueId = userUniqueId;
        return this;
    }

    public String getProperty() {
        return property;
    }

    public ProfileItem setProperty(String property) {
        property = property.toLowerCase();
        if (!Constants.PROFILE_PUBLIC_PROPERTIES.contains(property) && !property.startsWith("custom_")) {
            property = "custom_" + property;
        }
        this.property = property;
        return this;
    }

    public String getType() {
        return type;
    }

    public ProfileItem setType(String type) {
        this.type = type;
        return this;
    }

    public Map<String, Object> getValue() {
        return value;
    }

    public ProfileItem setValues(Map<String, Object> values) {
        this.value = values;
        return this;
    }

    public ProfileItem addValues(Object value) {
        if (value instanceof Integer) {
            this.value.put("as_integer", value);
            type = "integer";
        } else if (value instanceof String) {
            this.value.put("as_string", value);
            type = "string";
        }
        return this;
    }
}
