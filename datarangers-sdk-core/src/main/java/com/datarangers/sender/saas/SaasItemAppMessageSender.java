package com.datarangers.sender.saas;

import com.datarangers.config.DataRangersSDKConfigProperties;
import com.datarangers.config.EventConfig;
import com.datarangers.config.OpenapiConfig;
import com.datarangers.config.RangersJSONConfig;
import com.datarangers.event.Event;
import com.datarangers.event.EventV3;
import com.datarangers.message.AppMessage;
import com.datarangers.message.Message;
import com.datarangers.message.saas.SaasItemAppMessage;
import com.datarangers.sender.MessageSender;
import com.datarangers.util.AuthUtils;
import com.datarangers.util.HttpUtils;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author zhangpeng.spin@bytedance.com
 * @Date 2021-07-22
 */
public class SaasItemAppMessageSender implements MessageSender {

  private static final String path = "/dataprofile/openapi/v1/%s/items/%s/%s/attributes";

  @Override
  public void senderMessage(Message message, DataRangersSDKConfigProperties sdkConfigProperties) {
    AppMessage appMessage = message.getAppMessage();
    List<Event> events = appMessage.getEvents();

    OpenapiConfig openapiConfig = sdkConfigProperties.getOpenapiConfig();
    for (Event event : events) {
      EventV3 eventV3 = (EventV3) event;
      String item_name = String.valueOf(eventV3.getParams().get("item_name"));
      String item_id = String.valueOf(eventV3.getParams().get("item_id"));
      SaasItemAppMessage sendMessage = new SaasItemAppMessage(eventV3);
      String uriPath = String.format(path, appMessage.getAppId(), item_name, item_id);
      String url = openapiConfig.getDomain()
          + uriPath;
      String body = RangersJSONConfig.getInstance().toJson(sendMessage);
      String method = "PUT";

      String authorization = AuthUtils
          .sign(openapiConfig.getAk(), openapiConfig.getSk(), 1800, method, uriPath, null, body);
      Map<String, String> headers = new LinkedHashMap<>(EventConfig.SEND_HEADER);
      headers.put("Authorization", authorization);

      HttpUtils.request(method, url, body, headers);

    }
  }
}
