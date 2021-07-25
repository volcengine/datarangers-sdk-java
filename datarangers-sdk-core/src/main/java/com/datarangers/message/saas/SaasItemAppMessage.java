package com.datarangers.message.saas;

import com.datarangers.event.EventV3;
import com.datarangers.profile.ItemMethod;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * item 的消息格式
 *
 * @Author zhangpeng.spin@bytedance.com
 * @Date 2021-07-22
 */
public class SaasItemAppMessage extends SaasProfileAppMessage {

  private final static Map<String, String> operationMap = new HashMap() {{
    put(ItemMethod.SET.toString(), "SET");
    put(ItemMethod.DELETE.toString(), "DELETE");
    put(ItemMethod.UNSET.toString(), "UNSET");
  }};

  public SaasItemAppMessage() {
    super();
  }

  public SaasItemAppMessage(EventV3 eventV3) {
    for (Entry<String, Object> entry : eventV3.getParams().entrySet()) {
      String key = entry.getKey();
      if("item_id".equals(key) || "item_name".equals(key)){
        continue;
      }
      addAttribute(key, entry.getValue(), eventV3.getEvent());
    }
  }

  @Override
  protected String operationConvert(String method) {
    return operationMap.get(method);
  }
}
