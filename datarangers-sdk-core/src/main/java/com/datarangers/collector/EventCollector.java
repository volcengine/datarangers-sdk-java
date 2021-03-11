/*
 * Copyright 2020 Beijing Volcano Engine Technology Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.datarangers.collector;

import com.datarangers.event.Event;
import com.datarangers.event.Header;
import com.datarangers.event.Item;
import com.datarangers.profile.ProfileMethod;

import java.util.List;
import java.util.Map;

/**
 * @author hTangle
 */
public interface EventCollector {
    /**
     * 功能描述: 异步发送事件
     *
     * @param appId        应用id
     * @param custom       用户自定义公共参数
     * @param eventName    事件名称
     * @param eventParams  事件参数
     * @param userUniqueId 用户uuid
     * @return:  void
     * @date  2020/8/26 12:24
     */
    void sendEvent(String userUniqueId, int appId, Map<String, Object> custom, String eventName, Map<String, Object> eventParams);

    /**
     * 功能描述: 批量发送事件
     *
     * @param header 事件的公共属性，可以通过调用HeaderV3.Builder().build()构建一个header
     * @param events 事件数组 不推荐自己构建事件数组，我们推荐使用EventsBuilder这个类对多事件进行构造，调用build方法生成事件数组
     * @return: void
     * @date 2020/12/25 15:57
     */
    void sendEvents(Header header, List<Event> events);

    /**
     * 功能描述: 发送单条事件
     *
     * @param header      事件的公共属性，可以通过调用HeaderV3.Builder().build()构建一个header
     * @param eventName   事件名
     * @param eventParams 事件参数
     * @return: void
     * @date 2020/9/28 22:00
     */
    void sendEvent(Header header, String eventName, Map<String, Object> eventParams);

    /**
     * 功能描述: 批量发送事件
     *
     * @param header    事件的公共属性，可以通过调用HeaderV3.Builder().build()构建一个header
     * @param eventName 事件名数组，需要与eventParams数组长度相同
     * @param eventParams  需要设置的用户属性
     * @return: void
     * @date 2020/12/25 15:59
     */
    void sendEvent(Header header, List<String> eventName, List<Map<String, Object>> eventParams);

    /**
     * 功能描述: 对userUniqueId的用户进行profile属性设置
     *
     * @param userUniqueId 用户id
     * @param appId        app id
     * @param eventParams  需要设置的用户属性
     * @return: void
     * @date 2020/12/23 10:43
     */
    void profileSet(String userUniqueId, int appId, Map<String, Object> eventParams);

    void profileSetOnce(String userUniqueId, int appId, Map<String, Object> eventParams);

    void profileIncrement(String userUniqueId, int appId, Map<String, Object> eventParams);

    void profileAppend(String userUniqueId, int appId, Map<String, Object> eventParams);

    /**
     * 功能描述: 删除用户的属性
     * @param appId  app id
     * @param userUniqueId uuid
     * @param params 需要删除的用户属性名
     * @return: void
     * @date 2020/12/25 16:11
     */
    void profileUnset(String userUniqueId, int appId, List<String> params);

    /**
     * 功能描述: 对业务对象进行设置
     *
     * @param appId app id
     * @param name  业务对象的名称
     * @param items 业务对象的类，需要继承Items类,注意必须带上item_name
     * @return: void
     * @date: 2020/12/23 10:47
     */
    void itemSet(int appId, String name, List<Item> items);

    /**
     * 功能描述: 删除item的属性
     * @param appId app id
     * @param id 业务对象的编号
     * @param name 业务对象的名称
     * @param params 需要删除的item属性
     * @return: void
     * @date 2020/12/25 16:13
     */
    void itemUnset(int appId, String id, String name, List<String> params);

    @Deprecated
    void sendProfiles(String userUniqueId, int appId, List<ProfileMethod> profileModes, List<Map<String, Object>> profiles);
}
