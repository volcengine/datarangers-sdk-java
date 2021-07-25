/*
 * Copyright 2020 Beijing Volcano Engine Technology Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.datarangers.message;

import java.io.Serializable;

/**
 * 消息
 */
public class Message implements Serializable {

    /**
     * 消息环境
     */
    private MessageEnv messageEnv;
    /**
     * 消息类型
     */
    private MessageType messageType;
    /**
     * 消息内容
     */
    private AppMessage appMessage;

    public MessageEnv getMessageEnv() {
        return messageEnv;
    }

    public void setMessageEnv(MessageEnv messageEnv) {
        this.messageEnv = messageEnv;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public AppMessage getAppMessage() {
        return appMessage;
    }

    public void setAppMessage(AppMessage appMessage) {
        this.appMessage = appMessage;
    }

    public void merge() {
        if (this.appMessage != null) {
            this.appMessage.merge();
        }
    }

}
