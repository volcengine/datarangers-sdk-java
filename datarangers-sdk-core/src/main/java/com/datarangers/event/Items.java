/*
 * Copyright 2020 Beijing Volcano Engine Technology Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.datarangers.event;

import com.alibaba.fastjson.annotation.JSONField;

public class Items {
    @JSONField(name = "item_id")
    public String id;
    public String itemName;

    public Items(String id, String itemName) {
        this.id = id;
        this.itemName = itemName;
    }

    public String getId() {
        return id;
    }

    public Items setId(String id) {
        this.id = id;
        return this;
    }

    public String getItemName() {
        return itemName;
    }

    public Items setItemName(String itemName) {
        this.itemName = itemName;
        return this;
    }
}
