package com.datarangers.config;

/**
 * @Author zhangpeng.spin
 * @Date 2022/11/28
 */
public class VerifyConfig {
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "{" +
                "url='" + url + '\'' +
                '}';
    }
}
