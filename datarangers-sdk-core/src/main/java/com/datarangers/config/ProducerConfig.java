/*
 * Copyright 2020 Beijing Volcano Engine Technology Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.datarangers.config;

/**
 * @author zhangpeng.spin
 */
public class ProducerConfig {
    /**
     * 是否使用不阻塞的offer方法
     */
    private boolean useOffer;
    /**
     * offer timeout ,timeunit is 毫秒
     */
    private long offerTimeout;

    public boolean isUseOffer() {
        return useOffer;
    }

    public void setUseOffer(boolean useOffer) {
        this.useOffer = useOffer;
    }

    public long getOfferTimeout() {
        return offerTimeout;
    }

    public void setOfferTimeout(long offerTimeout) {
        this.offerTimeout = offerTimeout;
    }
}
