package com.datarangers.event;

import com.datarangers.config.Constants;
import com.datarangers.config.RangersJSONConfig;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author zhangpeng.spin@bytedance.com
 * @Date 2021-07-21
 */
public class SaasServerEvent {
  private String event;

  private String params;

  private String sessionId;

  private Long localTimeMs = System.currentTimeMillis();

  @JsonProperty("datetime")
  private String datetime = new SimpleDateFormat(Constants.FULL_DAY).format(new Date());

  private Long teaEventIndex;

  @JsonProperty("ab_sdk_version")
  private String abSdkVersion;

  public SaasServerEvent(){

  }

  public SaasServerEvent(EventV3 eventV3){
    this.setEvent(eventV3.getEvent());
    this.setSessionId(eventV3.getSessionId());
    this.setLocalTimeMs(eventV3.getLocalTimeMs());
    this.setDatetime(eventV3.getDatetime());
    this.setAbSdkVersion(eventV3.getAbSdkVersion());
    this.setTeaEventIndex(eventV3.getTeaEventIndex());
    this.setParams(RangersJSONConfig.getInstance().toJson(eventV3.getParams()));
  }

  public String getEvent() {
    return event;
  }

  public void setEvent(String event) {
    this.event = event;
  }

  public String getParams() {
    return params;
  }

  public void setParams(String params) {
    this.params = params;
  }

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  public Long getLocalTimeMs() {
    return localTimeMs;
  }

  public void setLocalTimeMs(Long localTimeMs) {
    this.localTimeMs = localTimeMs;
  }

  public String getDatetime() {
    return datetime;
  }

  public void setDatetime(String datetime) {
    this.datetime = datetime;
  }

  public Long getTeaEventIndex() {
    return teaEventIndex;
  }

  public void setTeaEventIndex(Long teaEventIndex) {
    this.teaEventIndex = teaEventIndex;
  }

  public String getAbSdkVersion() {
    return abSdkVersion;
  }

  public void setAbSdkVersion(String abSdkVersion) {
    this.abSdkVersion = abSdkVersion;
  }
}
