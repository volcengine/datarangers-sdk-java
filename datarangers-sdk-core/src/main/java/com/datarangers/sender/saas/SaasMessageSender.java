package com.datarangers.sender.saas;

import com.datarangers.config.DataRangersSDKConfigProperties;
import com.datarangers.message.Message;
import com.datarangers.message.MessageType;
import com.datarangers.sender.MessageSender;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhangpeng.spin
 */
public class SaasMessageSender implements MessageSender {
    private Map<MessageType, MessageSender> senders = new HashMap<>();

    public SaasMessageSender() {
        senders.put(MessageType.EVENT, new SaasServerAppMessageSender());
        senders.put(MessageType.PROFILE, new SaasProfileAppMessageSender());
        senders.put(MessageType.ITEM, new SaasItemAppMessageSender());
    }

    @Override
    public void send(Message message, DataRangersSDKConfigProperties sdkConfigProperties) {
        MessageSender messageSender = doGetSender(message.getMessageType());
        messageSender.send(message, sdkConfigProperties);
    }

    @Override
    public void sendBatch(List<Message> messages, DataRangersSDKConfigProperties sdkConfigProperties) {
        // 根据类型进行分组
        Map<MessageType, List<Message>> messageListMap =
                messages.stream().collect(Collectors.groupingBy(Message::getMessageType));
        for (Map.Entry<MessageType, List<Message>> entry : messageListMap.entrySet()) {
            List<Message> messageList = entry.getValue();
            if (messageList == null || messageList.isEmpty()) {
                continue;
            }
            MessageSender messageSender = doGetSender(entry.getKey());
            messageSender.sendBatch(messageList, sdkConfigProperties);
        }
    }

    protected MessageSender doGetSender(MessageType messageType) {
        MessageSender messageSender = senders.get(messageType);
        if (messageSender == null) {
            throw new IllegalArgumentException("Not support message: " + messageType);
        }
        return messageSender;
    }
}
