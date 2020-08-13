/*
 * Copyright 2020 Beijing Volcano Engine Technology Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.datarangers.collector;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.datarangers.config.Constants;
import com.datarangers.config.DataRangersSDKConfigProperties;
import com.datarangers.config.EventConfig;
import com.datarangers.event.*;
import com.datarangers.message.AppMessage;
import com.datarangers.message.Message;
import com.datarangers.message.ProfileMessage;
import com.datarangers.profile.ItemMethod;
import com.datarangers.profile.ProfileItem;
import com.datarangers.profile.ProfileMethod;
import com.datarangers.profile.ProfileRequestType;

import java.util.*;

public class AppEventCollector extends Collector {
    public AppEventCollector(String appType, DataRangersSDKConfigProperties properties) {
        setAppType(appType);
        if (properties != null) properties.init();
        else System.out.println(Constants.INIT_ERROR);
    }

    @Override
    public void sendEvent(String userUniqueId, int appId, Map<String, Object> custom, String eventName, Map<String, Object> eventParams) {
        if (eventParams == null) {
            logger.error("userUniqueId=" + userUniqueId + ",appId=" + appId + ",eventName=" + eventName + " params are null");
            return;
        }
        Header header = new HeaderV3.Builder().setCustom(custom).setAppId(appId).setUserUniqueId(userUniqueId).build();
        Event event = new EventV3().setEvent(eventName).setParams(eventParams).setUserId(userUniqueId);
        sendEvent(header, event);
    }

    @Override
    public void sendEvent(Header header, String eventName, Map<String, Object> eventParams) {
        if (eventParams == null) {
            logger.error("userUniqueId=" + header.getUserUniqueId() + ",appId=" + header.getAppId() + ",eventName=" + eventName + " params are null");
            return;
        }
        Event event = new EventV3().setEvent(eventName).setParams(eventParams).setUserId(header.getUserUniqueId());
        sendEvent(header, event);
    }

    @Override
    public void sendEvent(Header header, List<String> eventName, List<Map<String, Object>> eventParams) {
        List<Event> events = new ArrayList<>();
        for (int i = 0; i < Math.min(eventName.size(), eventParams.size()); i++) {
            if (eventParams.get(i) == null) continue;
            Event event = new EventV3().setEvent(eventName.get(i).toString()).setParams(eventParams.get(i)).setUserId(header.getUserUniqueId());
            events.add(event);
        }
        sendEvents(header, events);
    }

    private void sendEvents(Header header, EventsBuilder builder) {
        sendEvents(header, builder.build());
    }

    @Override
    @Deprecated
    public void profiles(String userUniqueId, int appId, List<ProfileItem> methods, List<Map<String, Object>> eventParams) {
        Header header = new HeaderV3.Builder().setAppId(appId).setUserUniqueId(userUniqueId).build();
        List<Event> events = new ArrayList<>();
        for (int i = 0; i < Math.min(methods.size(), eventParams.size()); i++) {
            Event event = new EventV3().setEvent(methods.get(i).toString()).setParams(eventParams.get(i)).setUserId(header.getUserUniqueId());
            events.add(event);
        }
        sendEvents(header, events);
    }

    private void profile(String userUniqueId, int appId, ProfileMethod method, Map<String, Object> eventParams) {
        if (eventParams == null) {
            logger.error("userUniqueId=" + userUniqueId + ",appId=" + appId + ",eventName=" + method.getMethod() + " params are null");
            return;
        }
        Header header = new HeaderV3.Builder().setAppId(appId).setUserUniqueId(userUniqueId).build();
        Event event = new EventV3().setEvent(method.toString()).setParams(eventParams).setUserId(userUniqueId);
        sendEvent(header, event);
    }

    @Override
    public void profileSet(String userUniqueId, int appId, Map<String, Object> eventParams) {
        profile(userUniqueId, appId, ProfileMethod.SET, eventParams);
    }

    @Override
    public void profileSetOnce(String userUniqueId, int appId, Map<String, Object> eventParams) {
        profile(userUniqueId, appId, ProfileMethod.SET_ONCE, eventParams);
    }

    @Override
    public void profileIncrement(String userUniqueId, int appId, Map<String, Object> eventParams) {
        profile(userUniqueId, appId, ProfileMethod.INCREMENT, eventParams);
    }

    @Override
    public void profileUnset(String userUniqueId, int appId, List<String> params) {
        Map<String, Object> eventParams = new HashMap<>();
        for (String p : params) eventParams.put(p, "java");
        profile(userUniqueId, appId, ProfileMethod.UNSET, eventParams);
    }

    @Override
    public void profileAppend(String userUniqueId, int appId, Map<String, Object> eventParams) {
        profile(userUniqueId, appId, ProfileMethod.APPEND, eventParams);
    }

    @Override
    public void itemSet(int appId, String itemName, List<Items> items) {
        item(ItemMethod.SET.toString(), appId, items);
    }

    @Override
    public void itemUnset(int appId, String id, String name, List<String> params) {
        Header header = new HeaderV3.Builder().setAppId(appId).setUserUniqueId(Constants.DEFAULT_USER).build();
        List<Event> events = new ArrayList<>();
        Map<String, Object> eventParams = new HashMap<>();
        for (String p : params) eventParams.put(p, "");
        events.add(new EventV3().
                setEvent(ItemMethod.UNSET.toString()).
                addParams("item_id", id).addParams("item_name", name).setParams(eventParams));
        sendEvents(header, events);
    }

    private void itemDelete(int appId, List<Items> items) {
        item(ItemMethod.DELETE.toString(), appId, items);
    }

    private void item(String eventName, int appId, List<Items> items) {
        if (items == null) {
            logger.error("appId=" + appId + ",eventName=" + eventName + " params are null");
            return;
        }
        Header header = new HeaderV3.Builder().setAppId(appId).setUserUniqueId(Constants.DEFAULT_USER).build();
        List<Event> events = new ArrayList<>();
        for (Items item : items) {
            if (item != null) {
                Event event = new EventV3().setEvent(eventName);
                ((JSONObject) JSON.toJSON(item, EventConfig.config)).forEach(event::addParams);
                events.add(event);
            }
        }
        sendEvents(header, events);
    }

    @Deprecated
    public void sendProfiles(String userUniqueId, int appId, List<ProfileMethod> profileModes, List<Map<String, Object>> profiles) {
        Header header = new HeaderV3.Builder().setAppId(appId).setUserUniqueId(userUniqueId).build();
        List<Event> events = new ArrayList<>();
        for (int i = 0; i < Math.min(profileModes.size(), profiles.size()); i++) {
            Event event = new EventV3().setEvent(profileModes.get(i).getMethod()).setParams(profiles.get(i));
            events.add(event);
        }
        sendEvents(header, events);
    }

    private void sendEvent(Header header, Event event) {
        Message message = new AppMessage();
        message.setUserUniqueId(header.getUserUniqueId());
        message.setAppType(getAppType());
        message.setHeader(header);
        message.addEvent(event);
        send(message);
    }

    public void sendEvents(Header header, List<Event> events) {
        Message message = new AppMessage();
        message.setUserUniqueId(header.getUserUniqueId());
        message.setAppType(getAppType());
        message.setHeader(header);
        message.addEvents(events);
        send(message);
    }

    @Deprecated
    public void setUserProfile(String userUniqueId, int appId, String propertyName, String value) {
        sendProfileRequest(appId, userUniqueId, ProfileRequestType.SET.toString(), propertyName, value);
    }

    @Deprecated
    public void setUserProfile(String userUniqueId, int appId, String propertyName, int value) {
        sendProfileRequest(appId, userUniqueId, ProfileRequestType.SET.toString(), propertyName, value);
    }

    @Deprecated
    public void appendUserProfile(String userUniqueId, int appId, String propertyName, String value) {
        sendProfileRequest(appId, userUniqueId, ProfileRequestType.APPEND.toString(), propertyName, value);
    }

    @Deprecated
    public void appendUserProfile(String userUniqueId, int appId, String propertyName, int value) {
        sendProfileRequest(appId, userUniqueId, ProfileRequestType.APPEND.toString(), propertyName, value);
    }

    @Deprecated
    public void setProfiles(List<ProfileItem> properties) {
        ProfileMessage message = new ProfileMessage();
        message.setItems(properties);
        sendMessage(message);
    }

    @Deprecated
    private void sendProfileRequest(int appId, String userUniqueId, String requestType, String propertyName, Object value) {
        ProfileMessage message = new ProfileMessage();
        message.setItems(Arrays.asList(new ProfileItem().
                setAppId(appId).setRequestType(requestType).
                setUserUniqueId(userUniqueId).
                setProperty(propertyName).addValues(value)));
        sendMessage(message);
    }
}