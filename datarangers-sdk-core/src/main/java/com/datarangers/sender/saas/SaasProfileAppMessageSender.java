package com.datarangers.sender.saas;

import com.datarangers.config.DataRangersSDKConfigProperties;
import com.datarangers.config.EventConfig;
import com.datarangers.config.OpenapiConfig;
import com.datarangers.config.RangersJSONConfig;
import com.datarangers.message.AppMessage;
import com.datarangers.message.Message;
import com.datarangers.message.saas.SaasProfileAppMessage;
import com.datarangers.sender.MessageSender;
import com.datarangers.util.AuthUtils;
import com.datarangers.util.HttpUtils;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author zhangpeng.spin@bytedance.com
 * @Date 2021-07-22
 */
public class SaasProfileAppMessageSender implements MessageSender {

  private String path = "/dataprofile/openapi/v1/%s/users/%s/attributes";

  @Override
  public void senderMessage(Message message, DataRangersSDKConfigProperties sdkConfigProperties) {
    SaasProfileAppMessage sendMessage = new SaasProfileAppMessage(message);
    String body = RangersJSONConfig.getInstance().toJson(sendMessage);

    AppMessage appMessage = message.getAppMessage();

    OpenapiConfig openapiConfig = sdkConfigProperties.getOpenapiConfig();
    String uriPath = String.format(path, appMessage.getAppId(), appMessage.getUserUniqueId());
    String url = openapiConfig.getDomain() + uriPath;

    String method = "PUT";
    String authorization = AuthUtils
        .sign(openapiConfig.getAk(), openapiConfig.getSk(), 1800, method, uriPath, null, body);
    Map<String, String> headers = new LinkedHashMap<>(EventConfig.SEND_HEADER);
    headers.put("Authorization", authorization);

    HttpUtils.request(method, url, body, headers);

  }

}
