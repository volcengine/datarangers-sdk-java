package com.datarangers.sender;

import com.datarangers.config.DataRangersSDKConfigProperties;
import com.datarangers.config.EventConfig;
import com.datarangers.config.RangersJSONConfig;
import com.datarangers.message.AppMessage;
import com.datarangers.message.Message;
import com.datarangers.util.HttpUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author zhangpeng.spin@bytedance.com
 * @Date 2021-07-22
 */
public class PrivatizationMessageSender implements MessageSender {

  @Override
  public void send(Message message, DataRangersSDKConfigProperties sdkConfigProperties) {
    Object sendMessage = message.getAppMessage();
    HttpUtils.post(EventConfig.getAppUrl(), RangersJSONConfig.getInstance().toJson(sendMessage), EventConfig.SEND_HEADER);
  }

  @Override
  public void sendBatch(List<Message> message, DataRangersSDKConfigProperties sdkConfigProperties) {
    List<AppMessage> sendMessages = message.stream().map(n -> n.getAppMessage()).collect(Collectors.toList());
    HttpUtils.post(EventConfig.getAppListUrl(), RangersJSONConfig.getInstance().toJson(sendMessages), EventConfig.SEND_HEADER);
  }
}
