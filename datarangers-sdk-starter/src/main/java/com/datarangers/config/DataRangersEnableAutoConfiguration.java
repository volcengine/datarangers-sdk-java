/*
 * Copyright 2020 Beijing Volcano Engine Technology Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.datarangers.config;

import com.datarangers.collector.AppEventCollector;
import com.datarangers.collector.EventCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
/**
 * @Description: TODO
 * @Author: bytedance
 * @Date 2020/12/2 20:07
 **/
@Configuration
@EnableAsync
@EnableConfigurationProperties(DataRangersSDKConfigPropertiesInfo.class)
public class DataRangersEnableAutoConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(DataRangersSDKConfigPropertiesInfo.class);
    @Autowired
    private DataRangersSDKConfigPropertiesInfo dataRangersSDKConfigPropertiesInfo;

    @Bean(name = "appEventCollector")
    @ConditionalOnProperty(prefix = "datarangers.sdk", name = "enable", havingValue = "true")
    public EventCollector defaultAppCollector() {
        return new AppEventCollector("app",dataRangersSDKConfigPropertiesInfo);
    }

    @Bean(name = "webEventCollector")
    @ConditionalOnProperty(prefix = "datarangers.sdk", name = "enable", havingValue = "true")
    public EventCollector defaultWebCollector() {
        return new AppEventCollector("web",dataRangersSDKConfigPropertiesInfo);
    }

    @Bean(name = "mpEventCollector")
    @ConditionalOnProperty(prefix = "datarangers.sdk", name = "enable", havingValue = "true")
    public EventCollector defaultMpbCollector() {
        return new AppEventCollector("mp",dataRangersSDKConfigPropertiesInfo);
    }
}
