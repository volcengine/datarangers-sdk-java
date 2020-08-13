package com.datarangers.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description: TODO
 * @Author: hezhiwei.alden@bytedance.com
 * @Date 2020/12/23 13:18
 **/
public class EventsBuilder {
    List<Event> eventList = new ArrayList<>();

    public EventsBuilder addEvent(String eventName, Map<String, Object> eventParams) {
        Event event = new EventV3().setEvent(eventName).setParams(eventParams);
        eventList.add(event);
        return this;
    }

    public List<Event> build() {
        return this.eventList;
    }
}
