# DataRangers
## 项目背景
datarangers-sdk-java 是 [DataFinder](https://www.volcengine.com/product/datafinder) 的用户行为采集服务端SDK。

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
  <version>t7-1.5.7-release</version>
</dependency>
```

火山引擎仓库地址：
```xml
<repositories>
  <repository>
    <id>bytedance-volcengine</id>
    <name>bytedance Volcengine</name>
    <url>https://artifact.bytedance.com/repository/Volcengine/</url>
  </repository>
</repositories>
```

### 2. 使用方式
参考官方文档 [DataFinder](https://www.volcengine.com/docs/6285/75430)

### 3. Demo
参考 [datarangers-sdk-example](https://github.com/volcengine/datarangers-sdk-java/tree/main/datarangers-sdk-example) 代码样例


## 注意事项
* 当前sdk版本没有主动清理日志的功能，需要手动清理日志
* 上报事件，需要注意下事件发生时间


## License
Copyright 2020 Beijing Volcano Engine Technology Co., Ltd.

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. 
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
