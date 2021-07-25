package com.datarangers.event;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @Author zhangpeng.spin@bytedance.com
 * @Date 2021-07-21
 */
public class User {
  @JsonProperty("user_unique_id")
  private String userUniqueId;

  public String getUserUniqueId() {
    return userUniqueId;
  }

  public void setUserUniqueId(String userUniqueId) {
    this.userUniqueId = userUniqueId;
  }
}
