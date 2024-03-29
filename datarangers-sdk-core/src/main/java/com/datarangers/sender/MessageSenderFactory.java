package com.datarangers.sender;

import com.datarangers.message.Message;
import com.datarangers.message.MessageEnv;
import com.datarangers.message.MessageType;
import com.datarangers.sender.saas.SaasItemAppMessageSender;
import com.datarangers.sender.saas.SaasProfileAppMessageSender;
import com.datarangers.sender.saas.SaasServerAppMessageSender;
import com.datarangers.sender.saasnative.SaasNativeMessageSender;

/**
 * @Author zhangpeng.spin@bytedance.com
 * @Date 2021-07-22
 */
public class MessageSenderFactory {

    private MessageSenderFactory() {
    }

    public static MessageSender getMessageSender(Message message) {
        MessageEnv messageEnv = message.getMessageEnv();
        if (MessageEnv.PRIVATIZATION == messageEnv) {
            return new PrivatizationMessageSender();
        }
        if (MessageEnv.SAAS_NATIVE == messageEnv) {
            return new SaasNativeMessageSender();
        }
        MessageType messageType = message.getMessageType();
        switch (messageType) {
            case EVENT:
                return new SaasServerAppMessageSender();
            case ITEM:
                return new SaasItemAppMessageSender();
            case PROFILE:
                return new SaasProfileAppMessageSender();
            default:
                throw new IllegalArgumentException("Not support message: " + messageType);
        }
    }
}
