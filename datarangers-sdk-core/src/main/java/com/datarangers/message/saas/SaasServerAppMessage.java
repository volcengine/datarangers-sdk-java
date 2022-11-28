package com.datarangers.message.saas;

import com.datarangers.event.Event;
import com.datarangers.event.EventV3;
import com.datarangers.event.HeaderV3;
import com.datarangers.event.SaasServerEvent;
import com.datarangers.event.User;
import com.datarangers.message.AppMessage;
import com.datarangers.message.Message;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author zhangpeng.spin@bytedance.com
 * @Date 2021-07-21
 */
public class SaasServerAppMessage extends DefaultSaasServerAppMessage{

  public SaasServerAppMessage(Message message) {
    super(message);

    this.getHeader().setAid(null);
    this.getHeader().setAppId(null);
  }

}
