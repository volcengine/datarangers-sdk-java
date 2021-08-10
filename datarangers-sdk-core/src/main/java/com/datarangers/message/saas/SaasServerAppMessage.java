package com.datarangers.message.saas;

import com.datarangers.event.Event;
import com.datarangers.event.EventV3;
import com.datarangers.event.HeaderV3;
import com.datarangers.event.SaasServerEvent;
import com.datarangers.event.User;
import com.datarangers.message.AppMessage;
import com.datarangers.message.Message;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author zhangpeng.spin@bytedance.com
 * @Date 2021-07-21
 */
public class SaasServerAppMessage {

  @JsonProperty("user")
  private User user = new User();

  @JsonProperty("header")
  private HeaderV3 header = (HeaderV3) new HeaderV3.Builder().build();

  @JsonProperty("events")
  private List<SaasServerEvent> events = new ArrayList<>();

  public SaasServerAppMessage() {

  }

  public SaasServerAppMessage(Message message) {

    AppMessage appMessage = message.getAppMessage();
    this.getUser().setUserUniqueId(appMessage.getUserUniqueId());
    this.setHeader(appMessage.getHeader());
    List<Event> appEvents = appMessage.getEvents();
    if (appEvents != null) {
      for(Event event:appEvents){
        events.add(new SaasServerEvent((EventV3) event));
      }
    }
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public HeaderV3 getHeader() {
    return header;
  }

  public void setHeader(HeaderV3 header) {
    this.header = header;
  }

  public List<SaasServerEvent> getEvents() {
    return events;
  }

  public void setEvents(List<SaasServerEvent> events) {
    this.events = events;
  }
}
