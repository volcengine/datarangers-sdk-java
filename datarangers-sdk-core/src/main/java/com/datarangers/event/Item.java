/*
 * Copyright 2020 Beijing Volcano Engine Technology Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.datarangers.event;
/**
 * 功能描述: if you use item in DataRangers, you should define item in DataRangers before using it
 *         example: the item has properties {id,item_name,title,price}
 *         then you should define a class like
 *         class BookItem implements Item{
 *             private String item_id;// =>id
 *             private String item_name;// =>item_name
 *             private String title;
 *             private float price;
 *         }
 *
 * Note: the id in DataRangers is the same as item_id in sdk
 *
 * @date: 2021/2/10 10:06
 */
public interface Item {
    /**
     * 功能描述: get item id
     *
     * @return: java.lang.String
     * @date: 2021/2/10 10:05
     */
    String getItemId();

    /**
     * 功能描述: get item name
     *
     * @param: []
     * @return: java.lang.String
     * @date: 2021/2/10 10:05
     */
    String getItemName();
}
