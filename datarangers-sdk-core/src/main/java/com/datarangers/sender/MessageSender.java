package com.datarangers.sender;

import com.datarangers.config.DataRangersSDKConfigProperties;
import com.datarangers.message.Message;

/**
 * @Author zhangpeng.spin@bytedance.com
 * @Date 2021-07-22
 */
public interface MessageSender {

  /**
   * 发送消息
   * @param message
   */
  void senderMessage(Message message, DataRangersSDKConfigProperties sdkConfigProperties);
}
