package com.datarangers.sender.saas;

import com.datarangers.config.Constants;
import com.datarangers.config.DataRangersSDKConfigProperties;
import com.datarangers.config.EventConfig;
import com.datarangers.config.RangersJSONConfig;
import com.datarangers.message.Message;
import com.datarangers.message.saas.SaasServerAppMessage;
import com.datarangers.sender.MessageSender;
import com.datarangers.util.HttpUtils;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author zhangpeng.spin@bytedance.com
 * @Date 2021-07-22
 */
public class SaasServerAppMessageSender implements MessageSender {

  private static final String path = "/v2/event/json";

  @Override
  public void send(Message message, DataRangersSDKConfigProperties sdkConfigProperties) {
    Object sendMessage = new SaasServerAppMessage(message);
    String url = sdkConfigProperties.getDomain() + path;
    Map<String, String> headers = new HashMap<>();
    headers.putAll(EventConfig.SEND_HEADER);
    Integer appId = message.getAppMessage().getAppId();
    Map<Integer, String> appKeys = sdkConfigProperties.getAppKeys();
    String appKey = appKeys.get(appId);
    if(appKey == null){
      throw new IllegalArgumentException("App key cannot be empty. app_id: " + appId);
    }
    headers.put(Constants.APP_KEY, appKey);
    HttpUtils.post(url, RangersJSONConfig.getInstance().toJson(sendMessage), headers);
  }
}
