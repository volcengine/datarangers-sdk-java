/*
 * Copyright 2020 Beijing Volcano Engine Technology Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.datarangers.event;
import java.io.Serializable;
import java.util.Map;

public interface Event extends Serializable {
    /**
     * 功能描述: 设置事件名
     *
     * @param event 事件名
     * @param: [event]
     * @return: com.datarangers.event.Event
     * @date: 2020/8/26 15:16
     */
    Event setEvent(String event);

    /**
     * 功能描述: 设置事件属性
     *
     * @param params 事件属性
     * @param: [params]
     * @return: com.datarangers.event.Event
     * @date: 2020/8/26 15:17
     */
    Event setParams(Map<String, Object> params);
    Event addParams(String key,Object value);
}
