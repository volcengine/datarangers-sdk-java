/*
 * Copyright 2020 Beijing Volcano Engine Technology Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.datarangers.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EventsBuilder {
    List<Event> eventList = new ArrayList<>();
    /**
     * 功能描述: 增加事件
     *
     * @param: [eventName, eventParams]
     * @return: com.datarangers.event.EventsBuilder
     * @date: 2021/3/5 14:56
     */
    public EventsBuilder addEvent(String eventName, Map<String, Object> eventParams) {
        Event event = new EventV3().setEvent(eventName).setParams(eventParams);
        eventList.add(event);
        return this;
    }

    /**
     * 功能描述: 增加事件
     *
     * @param eventName 事件名
     * @param eventParams 事件参数
     * @param localTimeMs 事件发生的时间
     * @return: com.datarangers.event.EventsBuilder
     * @date: 2021/3/5 14:56
     */
    public EventsBuilder addEvent(String eventName, Map<String, Object> eventParams, long localTimeMs) {
        Event event = new EventV3().setEvent(eventName).setParams(eventParams).setLocalTimeMs(localTimeMs);
        eventList.add(event);
        return this;
    }

    public List<Event> build() {
        return this.eventList;
    }
}
