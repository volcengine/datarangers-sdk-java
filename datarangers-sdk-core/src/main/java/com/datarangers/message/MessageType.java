package com.datarangers.message;

/**
 * 消息类型
 *
 * @Author zhangpeng.spin@bytedance.com
 * @Date 2021-07-22
 */
public enum MessageType {
  /**
   * 事件消息
   */
  EVENT,

  /**
   * 用户消息
   */
  PROFILE,

  /**
   * 业务对象消息
   */
  ITEM
}
