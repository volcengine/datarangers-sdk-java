package com.datarangers.event;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @Description: TODO
 * @Author: hezhiwei.alden@bytedance.com
 * @Date 2020/12/17 17:52
 **/
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
