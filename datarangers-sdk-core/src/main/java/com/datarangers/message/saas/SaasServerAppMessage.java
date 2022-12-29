package com.datarangers.message.saas;

import com.datarangers.message.Message;

/**
 * @Author zhangpeng.spin@bytedance.com
 * @Date 2021-07-21
 */
public class SaasServerAppMessage extends DefaultSaasServerAppMessage {

    public SaasServerAppMessage(Message message) {
        super(message);

        this.getHeader().setAid(null);
        this.getHeader().setAppId(null);
    }

}
