/*
 * Copyright 2020 Beijing Volcano Engine Technology Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.datarangers.config;


import com.datarangers.util.Tools;

/**
 * @author bytedance
 * @date 2020/12/2 20:02
 **/
public class HttpConfig {
  /**
   * 连接池最大连接数
   */
  private Integer maxTotal = 1000;
  private Integer maxPerRoute = 100;

  private Integer requestTimeout = 10 * 1000;
  private Integer connectTimeout = 10 * 1000;
  private Integer socketTimeout = 20 * 1000;

  /**
   * 单位是s，默认1分钟
   */
  private Integer keepAliveTimeout = 30;

  /**
   * 重试次数
   */
  private Integer retryCount;

  /**
   * 是否需要自定义配置key, store 路径和密码
   */
  private boolean customKeyTrustEnable;

  private String keyMaterialPath;
  private String keyPassword;
  private String storePassword;
  private String trustMaterialPath;
  private String trustStrategy;

  /**
   * 是否双向认证
   */
  private boolean trustDisable;

  public Integer getRequestTimeout() {
    return requestTimeout;
  }

  public void setRequestTimeout(Integer requestTimeout) {
    this.requestTimeout = requestTimeout;
  }

  public Integer getConnectTimeout() {
    return connectTimeout;
  }

  public void setConnectTimeout(Integer connectTimeout) {
    this.connectTimeout = connectTimeout;
  }

  public Integer getSocketTimeout() {
    return socketTimeout;
  }

  public void setSocketTimeout(Integer socketTimeout) {
    this.socketTimeout = socketTimeout;
  }

  public boolean isCustomKeyTrustEnable() {
    return customKeyTrustEnable;
  }

  public void setCustomKeyTrustEnable(boolean customKeyTrustEnable) {
    this.customKeyTrustEnable = customKeyTrustEnable;
  }


  public String getKeyMaterialPath() {
    return keyMaterialPath;
  }

  public void setKeyMaterialPath(String keyMaterialPath) {
    this.keyMaterialPath = keyMaterialPath;
  }

  public String getKeyPassword() {
    return keyPassword;
  }

  public void setKeyPassword(String keyPassword) {
    this.keyPassword = keyPassword;
  }

  public String getStorePassword() {
    return storePassword;
  }

  public void setStorePassword(String storePassword) {
    this.storePassword = storePassword;
  }

  public String getTrustMaterialPath() {
    return trustMaterialPath;
  }

  public void setTrustMaterialPath(String trustMaterialPath) {
    this.trustMaterialPath = trustMaterialPath;
  }

  public String getTrustStrategy() {
    return trustStrategy;
  }

  public void setTrustStrategy(String trustStrategy) {
    this.trustStrategy = trustStrategy;
  }

  public boolean isTrustDisable() {
    return trustDisable;
  }

  public void setTrustDisable(boolean trustDisable) {
    this.trustDisable = trustDisable;
  }

  @Deprecated
  public void initTimeOut(int timeBase) {
    if (requestTimeout == null) {
      requestTimeout = timeBase;
    }
    if (connectTimeout == null) {
      connectTimeout = timeBase;
    }
    if (socketTimeout == null) {
      socketTimeout = timeBase * 2;
    }

    if(keepAliveTimeout == null) {
      // 3分钟
      keepAliveTimeout = 30;
    }
  }

  public Integer getMaxTotal() {
    return maxTotal;
  }

  public void setMaxTotal(Integer maxTotal) {
    this.maxTotal = maxTotal;
  }

  public Integer getMaxPerRoute() {
    return maxPerRoute;
  }

  public void setMaxPerRoute(Integer maxPerRoute) {
    this.maxPerRoute = maxPerRoute;
  }

  public Integer getKeepAliveTimeout() {
    return keepAliveTimeout;
  }

  public void setKeepAliveTimeout(Integer keepAliveTimeout) {
    this.keepAliveTimeout = keepAliveTimeout;
  }

  public Integer getRetryCount() {
    return retryCount;
  }

  public void setRetryCount(Integer retryCount) {
    this.retryCount = retryCount;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("{");

    sb.append(String.format("\"requestTimeout\": %s,", requestTimeout));
    sb.append(String.format("\"connectTimeout\": %s,", connectTimeout));
    sb.append(String.format("\"socketTimeout\": %s,", socketTimeout));
    sb.append(String.format("\"keepAliveTimeout\": %s,", keepAliveTimeout));
    sb.append(String.format("\"retryCount\": %s,", retryCount));
    sb.append(String.format("\"maxTotal\": %s,", maxTotal));
    sb.append(String.format("\"maxPerRoute\": %s,", maxPerRoute));
    sb.append(String.format("\"keyMaterialPath\": \"%s\",", keyMaterialPath));
    sb.append(String.format("\"keyPassword\": \"%s\",", Tools.passwordMask(keyPassword)));
    sb.append(String.format("\"trustMaterialPath\": \"%s\",", trustMaterialPath));
    sb.append(String.format("\"storePassword\": \"%s\"", Tools.passwordMask(storePassword)));

    sb.append("}");
    return sb.toString();
  }
}
