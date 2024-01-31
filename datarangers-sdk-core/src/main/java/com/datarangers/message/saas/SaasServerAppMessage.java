package com.datarangers.message.saas;

import com.datarangers.event.HeaderV3;
import com.datarangers.message.Message;

/**
 * @Author zhangpeng.spin@bytedance.com
 * @Date 2021-07-21
 */
public class SaasServerAppMessage extends DefaultSaasServerAppMessage {

    public SaasServerAppMessage(Message message) {
        super(message);

        HeaderV3 header = this.getHeader();
        HeaderV3 headerClone = header.clone();
        headerClone.setAid(null);
        headerClone.setAppId(null);
        setHeader(headerClone);
    }

}
