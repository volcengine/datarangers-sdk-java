package com.datarangers.sender;

import com.datarangers.config.DataRangersSDKConfigProperties;
import com.datarangers.config.EventConfig;
import com.datarangers.config.RangersJSONConfig;
import com.datarangers.message.Message;
import com.datarangers.util.HttpUtils;

/**
 * @Author zhangpeng.spin@bytedance.com
 * @Date 2021-07-22
 */
public class PrivatizationMessageSender implements MessageSender {

  @Override
  public void senderMessage(Message message, DataRangersSDKConfigProperties sdkConfigProperties) {
    Object sendMessage = message.getAppMessage();
    HttpUtils.post(EventConfig.getAppUrl(), RangersJSONConfig.getInstance().toJson(sendMessage), EventConfig.SEND_HEADER);
  }
}
