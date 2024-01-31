/*
 * Copyright 2020 Beijing Volcano Engine Technology Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package com.datarangers.example;

import com.datarangers.collector.EventCollector;
import com.datarangers.event.*;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author zhangpeng.spin@bytedance.com
 * @Date 2022/9/28
 */
public abstract class AbstractSdkExample {
    /**
     * 获取 app应用上报的EventCollector
     *
     * @return EventCollector
     */
    public abstract EventCollector getAppEventCollector();

    /**
     * 获取 web应用上报的EventCollector
     *
     * @return EventCollector
     */
    public abstract EventCollector getWebEventCollector();

    /**
     * 获取 mp应用上报的EventCollector
     *
     * @return EventCollector
     */
    public abstract EventCollector getMpEventCollector();

    /**
     * 上报事件
     *
     * @param userUniqueId 用户ID
     * @param appId        应用ID
     */
    public void sendEvent(String userUniqueId, int appId) {
        EventCollector appEventCollector = getAppEventCollector();
        appEventCollector
                .sendEvent(userUniqueId, appId, new HashMap<String, Object>() {{
                            put("platform", 1002);
                        }}, "test_event",
                        new HashMap<String, Object>() {{
                            put("date_time", new SimpleDateFormat("yyyyMMdd").format(new Date()));
                            put("current_time",
                                    new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(new Date()));
                            put("test_item_id", 1002);
                        }});

        appEventCollector
                .sendEvent(userUniqueId, appId, new HashMap<String, Object>() {{
                            put("platform", 1000);
                        }}, "test_event",
                        new HashMap<String, Object>() {{
                            put("$event_duration", 68);
                            put("test_item_id", 1000);
                        }});

        System.out.println("end");
    }

    /**
     * 传入自定义时间戳
     *
     * @param userUniqueId
     * @param appId
     */
    public void sendEventWithLocalTimeMs(String userUniqueId, int appId, long localTimeMs) {
        EventCollector appEventCollector = getAppEventCollector();
        appEventCollector
                .sendEvent(userUniqueId, appId, null, "test_event_java_sdk",
                        new HashMap<String, Object>() {{
                            put("date_time", new SimpleDateFormat("yyyyMMdd").format(new Date()));
                            put("current_time",
                                    new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(new Date()));
                        }},
                        localTimeMs
                );

        System.out.println("end");
    }

    /**
     * 上报事件，携带ab_sdk_version
     *
     * @param userUniqueId
     * @param appId
     */
    public void senEventWithAbSdk(String userUniqueId, int appId) {
        EventCollector appEventCollector = getAppEventCollector();
        Map<String, Object> custom = new HashMap<>();
        Map<String, Object> eventParams = new HashMap<>();

        for (int i = 0; i < 5; i++) {
            Header header = new HeaderV3.Builder().setCustom(custom).setAppId(appId)
                    .setUserUniqueId(userUniqueId).build();
            Event event1 = new EventV3().setEvent("test_ab_sdk")
                    .setParams(eventParams).setUserId(userUniqueId)
                    .setLocalTimeMs(new Date().getTime())
                    .setAbSdkVersion("12345");
            Event event2 = new EventV3().setEvent("test_ab_sdk")
                    .setParams(eventParams).setUserId(userUniqueId)
                    .setLocalTimeMs(new Date().getTime())
                    .setAbSdkVersion("12345");
            appEventCollector.sendEvents(header, Arrays.asList(event1, event2));
        }

    }

    /**
     * 上报预置的事件公共属性
     * @param userUniqueId
     * @param appId
     */
    public void senEventPresetCommonParams(String userUniqueId, int appId) {
        EventCollector appEventCollector = getAppEventCollector();
        Map<String, Object> eventParams = new HashMap<>();
        eventParams.put("date_time", new SimpleDateFormat("yyyyMMdd").format(new Date()));

        for (int i = 0; i < 5; i++) {
            Header header = new HeaderV3.Builder()
                    .setAppId(appId)
                    .setUserUniqueId(userUniqueId)
                    .setLatestReferrer("https://www.toutiao.com/article/7119336107199693345/")
                    .setLatestReferrerHost("www.toutiao.com")
                    .setDeviceManufacturer("huawei")
                    .setHeight("10px")
                    .setWidth("12px")
                    .setLatestSearchKeyword("search")
                    .setLatestTrafficSourceType("source")
                    .setUserUniqueIdType("phone_id")
                    .setAppChannel("华为应用市场")
                    .setAppRegion("cn")
                    .setRegion("CN.r")
                    .setAppVersion("6.14.2")
                    .setAppVersionMinor("6")
                    .setDeviceModel("iphone 10 pro Max")
                    .setOsName("ios")
                    .setOsVersion("2.0.0")
                    .setSdkVersion("6.14.1")
                    .setSdkLib("java")
                    .setClientIp("10.10.0.1")
                    .setNetworkType("5G")
                    .setCarrier("中国电信")
                    .setResolution("1080*1080")
                    .setAppLanguage("ZH")
                    .setPlatform("android")
                    .setBrowser("qq")
                    .setBrowserVersion("1.9")
                    .setPackages("com.bytedance")
                    .setAppPackage("com.bytedance.app")
                    .setDeviceBrand("huawei")
                    .setAccess("wifi")
                    .setNetworkType("wifi")
                    .build();
            Event event1 = new EventV3().setEvent("test_preset_sdk")
                    .setParams(eventParams).setUserId(userUniqueId)
                    .setLocalTimeMs(new Date().getTime());
            Event event2 = new EventV3().setEvent("test_preset_sdk")
                    .setParams(eventParams).setUserId(userUniqueId)
                    .setLocalTimeMs(new Date().getTime());
            appEventCollector.sendEvents(header, Arrays.asList(event1, event2));
        }

    }

    public void sendEventWithHeader(String userUniqueId, int appId) {
        EventCollector appEventCollector = getAppEventCollector();
        Map<String, Object> eventParams = new HashMap<>();
        eventParams.put("date_time", new SimpleDateFormat("yyyyMMdd").format(new Date()));
        Header header = new HeaderV3.Builder()
                .setAppId(appId)
                .setUserUniqueId(userUniqueId)
                .setLatestReferrer("https://www.toutiao.com/article/7119336107199693345/")
                .setLatestReferrerHost("www.toutiao.com")
                .setDeviceManufacturer("huawei")
                .setHeight("10px")
                .setWidth("12px")
                .setLatestSearchKeyword("search")
                .setLatestTrafficSourceType("source")
                .setUserUniqueIdType("phone_id")
                .setAppChannel("华为应用市场")
                .setAppRegion("cn")
                .setRegion("CN.r")
                .setAppVersion("6.14.2")
                .setAppVersionMinor("6")
                .setDeviceModel("iphone 10 pro Max")
                .setOsName("ios")
                .setOsVersion("2.0.0")
                .setSdkVersion("6.14.1")
                .setSdkLib("java")
                .setClientIp("10.10.0.1")
                .setNetworkType("5G")
                .setCarrier("中国电信")
                .setResolution("1080*1080")
                .setAppLanguage("ZH")
                .setPlatform("android")
                .setBrowser("qq")
                .setBrowserVersion("1.9")
                .setPackages("com.bytedance")
                .setAppPackage("com.bytedance.app")
                .setDeviceBrand("huawei")
                .setAccess("wifi")
                .setNetworkType("wifi")
                .build();
        for (int i = 0; i < 5; i++) {
//            Event event1 = new EventV3().setEvent("test_preset_sdk")
//                    .setParams(eventParams).setUserId(userUniqueId)
//                    .setLocalTimeMs(new Date().getTime());
//            Event event2 = new EventV3().setEvent("test_preset_sdk")
//                    .setParams(eventParams).setUserId(userUniqueId)
//                    .setLocalTimeMs(new Date().getTime());
//            appEventCollector.sendEvents(header, Arrays.asList(event1, event2));
            appEventCollector.sendEvent(header, "test_preset_sdk", eventParams);
        }

    }

    /**
     * 上报用户属性
     *
     * @param userUniqueId 用户id
     * @param appId        应用id
     */
    public void sendUserProfile(String userUniqueId, int appId) {
        EventCollector appEventCollector = getAppEventCollector();
        appEventCollector.profileSet(userUniqueId, appId, new HashMap<String, Object>() {{
            put("profile_a", "param_11");
            put("profile_b", "param_22");
            put("profile_c", "param_33");
            put("profile_d", "param_44");
        }});
    }

    /**
     * 上报item属性，需要先在系统创建item
     *
     * @param appId 应用ID
     */
    public void sendItemProfile(int appId) {
        EventCollector appEventCollector = getAppEventCollector();

        List<Item> items = new ArrayList<>();
        items.add(
                new BookItem("1000", "book")
                        .setName("Java")
                        .setPrice(100)
                        .setPublishDate("2010-10-11")
                        .setAuthors(Arrays.asList("zhangsan", "lisi"))
                        .setCategory("1"));
        items.add(
                new BookItem("1002", "book")
                        .setName("PHP")
                        .setPrice(100)
                        .setPublishDate("2021-07-20")
                        .setAuthors(Arrays.asList("zhangsan", "wanger"))
                        .setCategory("2"));

        appEventCollector.itemSet(appId, "book", items);
    }

    /**
     * 在事件中，携带item
     *
     * @param userUniqueId 用户id
     * @param appId        应用id
     */
    public void sendEventWithItem(String userUniqueId, int appId) {
        EventCollector appEventCollector = getAppEventCollector();
        final List<Item> items = new ArrayList<>();
        items.add(new BookItem("1000", "book"));
        items.add(new BookItem("1002", "book"));
        appEventCollector
                .sendEvent(userUniqueId, appId, null, "set_items", new HashMap<String, Object>() {{
                    put("param1", "params");
                    put("param2", items.get(0));
                    put("param3", items.get(1));
                }});
    }

    /**
     * item 对象
     */
    static class BookItem implements Item {

        private String itemId;
        private String itemName;

        private String name;
        private int price;
        private String publishDate;
        private List<String> authors;
        private String category;

        public BookItem() {

        }

        public BookItem(String itemId, String itemName) {
            this.itemId = itemId;
            this.itemName = itemName;
        }

        @Override
        public String getItemId() {
            return itemId;
        }

        @Override
        public String getItemName() {
            return itemName;
        }

        public String getName() {
            return name;
        }

        public BookItem setName(String name) {
            this.name = name;
            return this;
        }

        public int getPrice() {
            return price;
        }

        public BookItem setPrice(int price) {
            this.price = price;
            return this;
        }


        public List<String> getAuthors() {
            return authors;
        }

        public BookItem setAuthors(List<String> authors) {
            this.authors = authors;
            return this;
        }

        public String getCategory() {
            return category;
        }

        public BookItem setCategory(String category) {
            this.category = category;
            return this;
        }

        public String getPublishDate() {
            return publishDate;
        }

        public BookItem setPublishDate(String publishDate) {
            this.publishDate = publishDate;
            return this;
        }
    }
}
