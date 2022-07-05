package com.datarangers.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @Author zhangpeng.spin@bytedance.com
 * @Date 2022/7/5
 */
public enum SdkMode {
    FILE,
    HTTP,
    KAFKA;

    @JsonCreator
    public static SdkMode fromString(String key) {
        return key == null ? null : SdkMode.valueOf(key.toUpperCase());
    }

    @JsonValue
    public String getKey() {
        return this.name().toLowerCase();
    }
}
