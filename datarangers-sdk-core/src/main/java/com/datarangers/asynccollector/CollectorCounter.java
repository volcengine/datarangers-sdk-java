/*
 * Copyright 2020 Beijing Volcano Engine Technology Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.datarangers.asynccollector;

import com.datarangers.collector.Collector;
import com.datarangers.config.Constants;
import com.datarangers.config.RangersJSONConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 自动保存到文件
 * @Author: hezhiwei.alden@bytedance.com
 **/
public class CollectorCounter implements Runnable {
    private String countName;

    public CollectorCounter(String savePath) {
        countName = savePath + "/count.log";
    }

    @Override
    public void run() {
        File output = new File(countName);
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(output, true);
            Map<String, Object> status = new HashMap<String, Object>() {{
                put("history", CollectorContainer.SEND_HISTORY);
                put("queue_length", Collector.collectorContainer.size());
            }};
            stream.write((LocalDateTime.now().format(Constants.FULL_DAY) + " " + RangersJSONConfig.getInstance().toJson(status) + "\n").getBytes());
            stream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
