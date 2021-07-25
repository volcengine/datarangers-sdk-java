package com.datarangers.sender.saas;

import com.datarangers.config.DataRangersSDKConfigProperties;
import com.datarangers.config.EventConfig;
import com.datarangers.config.RangersJSONConfig;
import com.datarangers.message.Message;
import com.datarangers.message.saas.SaasServerAppMessage;
import com.datarangers.sender.MessageSender;
import com.datarangers.util.HttpUtils;

/**
 * @Author zhangpeng.spin@bytedance.com
 * @Date 2021-07-22
 */
public class SaasServerAppMessageSender implements MessageSender {

  private static final String path = "/v2/event/json";

  @Override
  public void senderMessage(Message message, DataRangersSDKConfigProperties sdkConfigProperties) {
    Object sendMessage = new SaasServerAppMessage(message);
    String url = sdkConfigProperties.getDomain() + path;
    HttpUtils.post(url, RangersJSONConfig.getInstance().toJson(sendMessage),
        EventConfig.SEND_HEADER);
  }
}
