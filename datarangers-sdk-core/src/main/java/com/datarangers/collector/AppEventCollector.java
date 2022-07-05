/*
 * Copyright 2020 Beijing Volcano Engine Technology Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.datarangers.collector;

import com.datarangers.asynccollector.Consumer;
import com.datarangers.config.Constants;
import com.datarangers.config.DataRangersSDKConfigProperties;
import com.datarangers.config.RangersJSONConfig;
import com.datarangers.event.*;
import com.datarangers.message.AppMessage;
import com.datarangers.message.Message;
import com.datarangers.message.MessageType;
import com.datarangers.profile.ItemMethod;
import com.datarangers.profile.ProfileMethod;

import com.datarangers.sender.Callback;
import java.io.IOException;
import java.util.*;

/**
 * @author hTangle
 */
public class AppEventCollector extends Collector {

  public AppEventCollector(String appType, DataRangersSDKConfigProperties properties) {
    this(appType, properties, null);
  }

  public AppEventCollector(String appType, DataRangersSDKConfigProperties properties, Callback cb) {
    super(appType, properties, cb);
    if (properties != null) {
      properties.init();

      // 设置同步发送的consumer，队列满的时候使用
      setConsumer(new Consumer(Collector.collectorContainer, this.properties));

    } else {
      System.out.println(Constants.INIT_ERROR);
    }
  }

  @Override
  public void sendEvent(String userUniqueId, int appId, Map<String, Object> custom,
      String eventName, Map<String, Object> eventParams) {
    sendEvent(userUniqueId, appId, custom, eventName, eventParams, System.currentTimeMillis());
  }

  @Override
  public void sendEvent(String userUniqueId, int appId, Map<String, Object> custom,
      String eventName, Map<String, Object> eventParams, long localTimeMs) {
    if (eventParams == null) {
      logger.error("userUniqueId=" + userUniqueId + ",appId=" + appId + ",eventName=" + eventName
          + " params are null");
      return;
    }
    Header header = new HeaderV3.Builder().setCustom(custom).setAppId(appId)
        .setUserUniqueId(userUniqueId).build();
    Event event = new EventV3().setEvent(eventName).setParams(eventParams).setUserId(userUniqueId)
        .setLocalTimeMs(localTimeMs);
    sendEvent(header, event, MessageType.EVENT);
  }

  @Override
  public void sendEvent(Header header, String eventName, Map<String, Object> eventParams) {
    sendEvent(header, eventName, eventParams, System.currentTimeMillis());
  }

  @Override
  public void sendEvent(Header header, String eventName, Map<String, Object> eventParams,
      long localTimeMs) {
    if (eventParams == null) {
      logger.error(
          "userUniqueId=" + header.getUserUniqueId() + ",appId=" + header.getAppId() + ",eventName="
              + eventName + " params are null");
      return;
    }
    Event event = new EventV3().setEvent(eventName).setParams(eventParams)
        .setUserId(header.getUserUniqueId()).setLocalTimeMs(localTimeMs);
    sendEvent(header, event, MessageType.EVENT);
  }

  @Override
  @Deprecated
  public void sendEvent(Header header, List<String> eventName,
      List<Map<String, Object>> eventParams) {
    List<Event> events = new ArrayList<>();
    for (int i = 0; i < Math.min(eventName.size(), eventParams.size()); i++) {
      if (eventParams.get(i) == null) {
        continue;
      }
      Event event = new EventV3().setEvent(eventName.get(i)).setParams(eventParams.get(i))
          .setUserId(header.getUserUniqueId());
      events.add(event);
    }
    sendEvents(header, events);
  }

  private void profile(String userUniqueId, int appId, ProfileMethod method,
      Map<String, Object> eventParams) {
    if (eventParams == null) {
      logger.error(
          "userUniqueId=" + userUniqueId + ",appId=" + appId + ",eventName=" + method.getMethod()
              + " params are null");
      return;
    }
    Header header = new HeaderV3.Builder().setAppId(appId).setUserUniqueId(userUniqueId).build();
    Event event = new EventV3().setEvent(method.toString()).setParams(eventParams)
        .setUserId(userUniqueId);
    sendEvent(header, event, MessageType.PROFILE);
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
    params.forEach(p -> eventParams.put(p, "java"));
    profile(userUniqueId, appId, ProfileMethod.UNSET, eventParams);
  }

  @Override
  public void profileAppend(String userUniqueId, int appId, Map<String, Object> eventParams) {
    profile(userUniqueId, appId, ProfileMethod.APPEND, eventParams);
  }

  @Override
  public void itemSet(int appId, String itemName, List<Item> items) {
    item(ItemMethod.SET.toString(), appId, items);
  }

  @Override
  public void itemUnset(int appId, String id, String name, List<String> params) {
    Header header = new HeaderV3.Builder().setAppId(appId).setUserUniqueId(Constants.DEFAULT_USER)
        .build();
    List<Event> events = new ArrayList<>();
    Map<String, Object> eventParams = new HashMap<>();
    params.forEach(p -> eventParams.put(p, ""));
    events.add(new EventV3().
        setEvent(ItemMethod.UNSET.toString()).
        addParams("item_id", id).addParams("item_name", name).setParams(eventParams));
    sendEvents(header, events, MessageType.ITEM);
  }

  private void itemDelete(int appId, List<Item> items) {
    item(ItemMethod.DELETE.toString(), appId, items);
  }

  private void item(String eventName, int appId, List<Item> items) {
    if (items == null) {
      logger.error("appId=" + appId + ",eventName=" + eventName + " params are null");
      return;
    }
    Header header = new HeaderV3.Builder().setAppId(appId).setUserUniqueId(Constants.DEFAULT_USER)
        .build();
    List<Event> events = new ArrayList<>();
    for (Item item : items) {
      if (item != null) {
        Event event = new EventV3().setEvent(eventName);
        try {
          RangersJSONConfig.getInstance()
              .fromJson(RangersJSONConfig.getInstance().toJson(item), Map.class)
              .forEach((key, value) -> event.addParams((String) key, value));
        } catch (IOException e) {
          e.printStackTrace();
        }
        events.add(event);
      }
    }
    sendEvents(header, events, MessageType.ITEM);
  }

  @SuppressWarnings("AliDeprecation")
  @Override
  @Deprecated
  public void sendProfiles(String userUniqueId, int appId, List<ProfileMethod> profileModes,
      List<Map<String, Object>> profiles) {
    Header header = new HeaderV3.Builder().setAppId(appId).setUserUniqueId(userUniqueId).build();
    List<Event> events = new ArrayList<>();
    for (int i = 0; i < Math.min(profileModes.size(), profiles.size()); i++) {
      Event event = new EventV3().setEvent(profileModes.get(i).getMethod())
          .setParams(profiles.get(i));
      events.add(event);
    }
    sendEvents(header, events, MessageType.PROFILE);
  }

  private void sendEvent(Header header, Event event, MessageType messageType) {
    Message message = new Message();
    message.setMessageEnv(this.properties.getMessageEnv());
    message.setMessageType(messageType);

    AppMessage appMessage = new AppMessage();
    appMessage.setUserUniqueId(header.getUserUniqueId());
    appMessage.setAppType(getAppType());
    appMessage.setHeader(header);
    appMessage.addEvent(event);

    message.setAppMessage(appMessage);

    send(message);
  }

  private void sendEvents(Header header, List<Event> events, MessageType messageType) {
    Message message = new Message();
    message.setMessageEnv(this.properties.getMessageEnv());
    message.setMessageType(messageType);

    AppMessage appMessage = new AppMessage();
    appMessage.setUserUniqueId(header.getUserUniqueId());
    appMessage.setAppType(getAppType());
    appMessage.setHeader(header);
    appMessage.addEvents(events);

    message.setAppMessage(appMessage);
    send(message);
  }

  @Override
  public void sendEvents(Header header, List<Event> events) {
    sendEvents(header, events, MessageType.EVENT);
  }
}
