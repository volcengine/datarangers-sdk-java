package com.datarangers.message.saas;

import com.datarangers.event.Event;
import com.datarangers.event.EventV3;
import com.datarangers.message.Message;
import com.datarangers.profile.ProfileMethod;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @Author zhangpeng.spin@bytedance.com
 * @Date 2021-07-22
 */
public class SaasProfileAppMessage {

  private final static Map<String, String> operationMap = new HashMap() {{
    put(ProfileMethod.SET.toString(), "SET");
    put(ProfileMethod.SET_ONCE.toString(), "SET_ONCE");
    put(ProfileMethod.APPEND.toString(), "APPEND");
    put(ProfileMethod.INCREMENT.toString(), "INCREASE");
    put(ProfileMethod.UNSET.toString(), "UNSET");
  }};

  public SaasProfileAppMessage() {

  }

  public SaasProfileAppMessage(Message message) {
    List<Event> events = message.getAppMessage().getEvents();
    for (Event event : events) {
      EventV3 eventV3 = (EventV3) event;
      for (Entry<String, Object> entry : eventV3.getParams().entrySet()) {
        String key = entry.getKey();
        if ("item_id".equals(key) || "item_name".equals(key)) {
          continue;
        }
        addAttribute(key, entry.getValue(), eventV3.getEvent());
      }
    }
  }

  private List<Attribute> attributes = new ArrayList<>();

  public List<Attribute> getAttributes() {
    return attributes;
  }

  public void setAttributes(
      List<Attribute> attributes) {
    this.attributes = attributes;
  }

  protected void addAttribute(String name, Object value, String method) {
    String operation = operationConvert(method);
    if (operation == null || operation.isEmpty()) {
      throw new IllegalArgumentException("Not support operation: " + method);
    }
    this.attributes.add(new Attribute(name, value, operation));
  }

  protected String operationConvert(String method) {
    return operationMap.get(method);
  }
}


