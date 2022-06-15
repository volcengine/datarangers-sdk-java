/*
 * Copyright 2020 Beijing Volcano Engine Technology Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.datarangers.config;

import com.datarangers.collector.AppEventCollector;
import com.datarangers.collector.EventCollector;
import com.datarangers.sender.Callback;
import com.datarangers.sender.callback.LoggingCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author bytedance
 * @date 2020/12/2 20:07
 **/
@Configuration
@EnableAsync
@EnableConfigurationProperties(DataRangersSDKConfigPropertiesInfo.class)
public class DataRangersEnableAutoConfiguration {

  @Autowired
  private DataRangersSDKConfigPropertiesInfo dataRangersSDKConfigPropertiesInfo;

  @Bean(name = "appEventCollector")
  public EventCollector defaultAppCollector(Callback callback) {
    return new AppEventCollector("app", dataRangersSDKConfigPropertiesInfo, callback);
  }

  @Bean(name = "webEventCollector")
  public EventCollector defaultWebCollector(Callback callback) {
    return new AppEventCollector("web", dataRangersSDKConfigPropertiesInfo, callback);
  }

  @Bean(name = "mpEventCollector")
  public EventCollector defaultMpbCollector(Callback callback) {
    return new AppEventCollector("mp", dataRangersSDKConfigPropertiesInfo, callback);
  }

  @Bean
  @ConditionalOnMissingBean(Callback.class)
  public Callback callback() {
    return new LoggingCallback(dataRangersSDKConfigPropertiesInfo.getEventSavePath(),
        "error-" + dataRangersSDKConfigPropertiesInfo.getEventSaveName(),
        dataRangersSDKConfigPropertiesInfo.getEventSaveMaxFileSize());
  }
}
