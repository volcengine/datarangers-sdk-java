package com.datarangers.sender.saasnative;

import com.datarangers.config.Constants;
import com.datarangers.config.DataRangersSDKConfigProperties;
import com.datarangers.config.EventConfig;
import com.datarangers.config.RangersJSONConfig;
import com.datarangers.message.AppMessage;
import com.datarangers.message.Message;
import com.datarangers.sender.MessageSender;
import com.datarangers.util.HttpUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author zhangpeng.spin@bytedance.com
 * @Date 2021-07-22
 */
public class SaasNativeMessageSender implements MessageSender {

    @Override
    public void send(Message message, DataRangersSDKConfigProperties sdkConfigProperties) {
        Object sendMessage = getSassNativeMessage(message);
        Map<String, String> headers = getHeaders(message, sdkConfigProperties);
        HttpUtils.post(EventConfig.getAppUrl(), RangersJSONConfig.getInstance().toJson(sendMessage), headers);
    }

    @Override
    public void sendBatch(List<Message> message, DataRangersSDKConfigProperties sdkConfigProperties) {
        Map<String, String> headers = getHeaders(message.get(0), sdkConfigProperties);
        List<AppMessage> sendMessages = message.stream().map(n -> getSassNativeMessage(n)).collect(Collectors.toList());
        HttpUtils.post(EventConfig.getAppListUrl(), RangersJSONConfig.getInstance().toJson(sendMessages), headers);
    }

    private Map<String, String> getHeaders(Message message, DataRangersSDKConfigProperties sdkConfigProperties) {
        Map<String, String> headers = new HashMap<>();
        headers.putAll(EventConfig.SEND_HEADER);
        Integer appId = message.getAppMessage().getAppId();
        Map<Integer, String> appKeys = sdkConfigProperties.getAppKeys();
        String appKey = appKeys.get(appId);
        if (appKey == null) {
            throw new IllegalArgumentException("App key cannot be empty. app_id: " + appId);
        }
        headers.put(Constants.APP_KEY, appKey);
        return headers;
    }

    public static AppMessage getSassNativeMessage(Message message) {
        AppMessage cloneAppMessage = message.getAppMessage().clone();

        // appid 设置为null
        cloneAppMessage.setAppId(null);
        cloneAppMessage.getHeader().setAppId(null);
        return cloneAppMessage;
    }
}
