package com.datarangers.sender;

import com.datarangers.config.DataRangersSDKConfigProperties;
import com.datarangers.message.Message;

import java.util.List;

/**
 * @Author zhangpeng.spin@bytedance.com
 * @Date 2021-07-22
 */
public interface MessageSender {

  /**
   * 发送消息
   * @param message
   * @param sdkConfigProperties
   */
  void send(Message message, DataRangersSDKConfigProperties sdkConfigProperties);

  /**
   * 使用批量上报
   * @param messages
   * @param sdkConfigProperties
   */
  default void sendBatch(List<Message> messages, DataRangersSDKConfigProperties sdkConfigProperties) {
    throw new UnsupportedOperationException("Not support batch");
  };
}
