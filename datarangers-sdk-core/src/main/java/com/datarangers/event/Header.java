/*
 * Copyright 2020 Beijing Volcano Engine Technology Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.datarangers.event;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.Serializable;

/**
 * 功能描述: 公共属性
 */
@JsonDeserialize(as = HeaderV3.class)
public interface Header extends Serializable {
    Integer getAppId();

    Long getDeviceId();

    Header setUserUniqueId(String userUniqueId);

    Header setDeviceId(Long deviceId);

    Header putCustom(String key, Object value);

    String getUserUniqueId();
}
