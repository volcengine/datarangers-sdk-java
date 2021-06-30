# DataRangers
## 项目背景
datarangers-sdk-java是 [DataRangers](https://datarangers.com.cn/) 的用户行为采集服务端SDK。

服务端埋点支持在客户的服务端进行埋点采集和上报，作为客户端埋点的补充或替代，其支持的典型场景包括：
1. 客户端埋点+服务端埋点组合：该场景下，服务端埋点一般用来弥补客户端埋点覆盖不到的部分数据，是目前最常见的使用场景。
2. 纯服务端埋点：所有的埋点收集和上报都由服务端完成，需要的客户端数据则由服务端收集和提取后上报到DataRangers。

## 使用方法
### 1. 引入jar包
如果您需要使用Java SDK，首先需要在pom文件中引入对应的jar：
```xml
<dependency>
  <groupId>com.datarangers</groupId>
  <artifactId>datarangers-sdk-core</artifactId>
  <version>1.4.2-release</version>
</dependency>
```

version是sdk的版本号，当前最新的版本为1.4.0-release。

### 2. 配置SDK
DataRangers SDK需要进行一定的参数配置才能够使用，具体需要配置的参数为：
* domain：datarangers的域名或者ip，支持http和https，例如为 http://www.datarangers.com，在私有化环境中，需要修改为对应的sdk上报域名或者使用DataRangers服务器的ip地址。
* save：bool型变量，表示是否保存到文件：
    * true：保存到文件，但是需要配置LogAgent完成事件上报功能,需要额外定义：
    * eventSaveName：保存日志的文件名，需要保证文件的写权限。
    * eventSavePath：保存日志的文件路径，需要保证写权限和创建文件的权限。
    * eventSaveMaxFileSize：表示需要保存的日志文件的最大文件大小，单位为MB。
    * eventFilePaths：表示需要保存的日志文件的位置，为一个字符串数组，数组中的每一个值都表示一个路径，用户将日志文件写到不同的文件夹下，可以配合多个LogAgent实例使用。注意：如果定义了该数组，则eventSavePath不会生效。
* false：使用http模式进行异步上报：
    * httpTimeout：Http的超时时间，单位为ms。
    * headers：Map类型，Http请求的Header中的字段，必填为Host ，Host在DataRangers安装中进行定义
    
如果您使用了Spring框架，则可以参考的配置如下：
```xml
<bean name="appEventCollector" class="com.datarangers.collector.AppEventCollector">
    <constructor-arg name="appType" value="app"/>
    <constructor-arg name="properties" ref="dataRangersSDKConfigProperties"/>
</bean>
<bean name="mpEventCollector" class="com.datarangers.collector.AppEventCollector">
    <constructor-arg name="appType" value="mp"/>
    <constructor-arg name="properties" ref="dataRangersSDKConfigProperties"/>
</bean>
<bean name="webEventCollector" class="com.datarangers.collector.AppEventCollector">
    <constructor-arg name="appType" value="web"/>
    <constructor-arg name="properties" ref="dataRangersSDKConfigProperties"/>
</bean>
<bean name="dataRangersSDKConfigProperties" class="com.datarangers.config.DataRangersSDKConfigProperties">
    <property name="domain" value="{domain}"/>
    <property name="save" value="false"/>
    <property name="headers">
        <map>
            <entry key="Host" value="{host}"/>
        </map>
    </property>
    <property name="eventSaveName" value="datarangers.log"/>
    <property name="eventSavePath" value="logs/"/>
    <property name="eventSaveMaxFileSize" value="1"/>
    <property name="httpTimeout" value="500"/>
</bean>
```

如果您使用SpringBoot框架，我们提供了一个封装完成的的starter包，您可以在pom中通过如下方式引入：
```xml
<dependency>
   <groupId>com.datarangers</groupId>
   <artifactId>datarangers-sdk-starter</artifactId>
   <version>1.4.0-release</version>
</dependency>
```

并在properties文件中对sdk进行配置
```properties
# 使能sdk功能,为false就disable sdk功能
datarangers.sdk.enable=true
# rangers的ip或域名
datarangers.sdk.domain=http://domain
# datarangers.sdk.headers为http请求中headers字段内容,必须要添加Host,其他如果需要设置的可以选填
datarangers.sdk.headers.Host=host


# 是否保存到本地,如果需要配合logagent使用需要将其定义为true
datarangers.sdk.save=true
# 异步方式的发送线程数量,如果为logagent模式请设置为1
datarangers.sdk.threadCount=4
# 异步方式队列长度
datarangers.sdk.queueSize=102400
# 保存日志文件路径
datarangers.sdk.eventSavePath=logs/
# 保存日志文件名
datarangers.sdk.eventSaveName=datarangers
# 最多保存的单个日志的大小,单位MB
datarangers.sdk.eventSaveMaxFileSize=256
```

### 3. 使用SDK
使用时需要先注入Bean，Bean有三种类型，如下：
```java
// App
@Resource(name = "appEventCollector")
private EventCollector appEventCollector;
// Web
@Resource(name = "webEventCollector")
private EventCollector webEventCollector;
// 小程序
@Resource(name = "mpEventCollector")
private EventCollector mpEventCollector;
```

如果您已经注入完成了，则可以调用bean进行事件发送。发送的接口为：
```java
/**
 * 功能描述: 异步发送事件
 *
 * @param appId        应用id
 * @param custom       用户自定义公共参数
 * @param eventName    事件名称
 * @param eventParams  事件参数
 * @param userUniqueId 用户uuid
 * @return: void
 * @date: 2020/8/26 12:24
 */
void sendEvent(String userUniqueId, int appId, Map<String, Object> custom, String eventName, Map<String, Object> eventParams);

/**
 * 功能描述: 批量发送事件
 *
 * @param header 事件的公共属性，可以通过调用HeaderV3.Builder().build()构建一个header
 * @param events 事件数组 一般不推荐自己构建事件数组，我们推荐使用EventsBuilder这个类对多事件进行构造，并调用build方法生成事件数组
 * @return: void
 * @date: 2020/12/25 15:57
 */
void sendEvents(Header header, List<Event> events);

/**
 * 功能描述: 发送单条事件
 *
 * @param header      事件的公共属性，可以通过调用HeaderV3.Builder().build()构建一个header
 * @param eventName   事件名
 * @param eventParams 事件参数
 * @return: void
 * @date: 2020/9/28 22:00
 */
void sendEvent(Header header, String eventName, Map<String, Object> eventParams);

/**
 * 功能描述: 批量发送事件
 *
 * @param header    事件的公共属性，可以通过调用HeaderV3.Builder().build()构建一个header
 * @param eventName 事件名数组，需要与eventParams数组长度相同
 * @return: void
 * @date: 2020/12/25 15:59
 */
void sendEvent(Header header, List<String> eventName, List<Map<String, Object>> eventParams);

/**
 * 功能描述: 对userUniqueId的用户进行profile属性设置
 *
 * @param appId        app id
 * @param userUniqueId 用户id
 * @param eventParams  需要设置的用户属性
 * @return: void
 * @date: 2020/12/23 10:43
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
 * @date: 2020/12/25 16:11
 */
void profileUnset(String userUniqueId, int appId, List<String> params);

/**
 * 功能描述: 对业务对象进行设置
 *
 * @param appId app id
 * @param name  业务对象的名称
 * @param items 业务对象的类，需要继承Items类,注意
 * @return: void
 * @date: 2020/12/23 10:47
 */
void itemSet(int appId, String name, List<Items> items);

/**
 * 功能描述: 删除item的属性
 * @param appId
 * @param id
 * @param name
 * @param params 需要删除的item属性
 * @return: void
 * @date: 2020/12/25 16:13
 */
void itemUnset(int appId, String id, String name, List<String> params);
```

## 3. 使用示例
1. 发送普通事件
```java
@Resource(name = "appEventCollector")
private EventCollector appEventCollector;

appEventCollector.sendEvent("uuid2", 10000000, null, "test_event_java_sdk", new HashMap<String, Object>() {{
    put("date_time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
    put("current_time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss")));
}});

```

2. 设置用户属性
```java
eventCollector.profileSet("uuid-1", 10000028, new HashMap<String, Object>() {{
    put("profile_1", "param_1");
    put("profile_2", "param_2");
    put("profile_3", "param_3");
    put("profile_4", "param_4");
}});
```

3. 设置Item属性
```java
List<Items> items = new ArrayList<>();
items.add(new BookItems("1000", "book").setName("Java").setPrice(100).setPublishDate(LocalDate.now()).setAuthors(author).setCategory("computer"));
items.add(new BookItems("1002", "book").setName("PHP").setPrice(100).setPublishDate(LocalDate.now()).setAuthors(author).setCategory("computer"));
eventCollector.itemSet(10000028, "book", items);
```

4. 发送携带item的事件
```java
List<Items> items = new ArrayList<>();
items.add(new BookItems("1000", "book"));
items.add(new BookItems("1002", "book"));
items.add(new PhoneItems("1002", "phone"));
eventCollector.sendEvent("user-001", 10000028, null, "set_items", new HashMap<String, Object>() {{
    put("param1", "params");
    put("param2", items.get(0));
    put("param3", items.get(1));
    put("param4", items.get(2));
}});
```

## 4. 注意事项
* 当前sdk版本没有主动清理日志的功能，需要手动清理日志