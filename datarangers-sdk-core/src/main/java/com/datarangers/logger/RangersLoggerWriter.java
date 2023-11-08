/*
 * Copyright 2020 Beijing Volcano Engine Technology Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.datarangers.logger;

import com.datarangers.config.Constants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RangersLoggerWriter implements RangersFileWriter {
    private final String targetName;
    private final String targetPrefix;
    private final String fullTarget;
    private File output;
    private FileOutputStream stream;
    private int count;
    private int maxSize;
    private String currentName;
    private int currentIndex = 0;
    private volatile int checkCount = 0;


    private static final Map<String, RangersLoggerWriter> instance = new HashMap<>();

    public static RangersLoggerWriter getInstance(final String targetPrefix, final String targetName, int maxSize) {
        synchronized (instance) {
            String key = targetPrefix + "/" + targetName;
            if (!instance.containsKey(key)) {
                instance.put(key, new RangersLoggerWriter(targetPrefix, targetName, maxSize));
            }
            RangersLoggerWriter writer = instance.get(key);
            writer.count++;
            return writer;
        }
    }

    public static void removeInstance(RangersLoggerWriter writer) {
        synchronized (instance) {
            writer.count--;
            if (writer.count == 0) {
                instance.remove(writer.targetName);
                writer.close();
            }
        }
    }


    private void changeOutputStream() {
        // 如果文件存在且大于限制大小
        if (output.exists() && output.length() / 1024 / 1024 > maxSize) {
            //文件名应该一直叫targetName
            String currentHour = targetName + "." + LocalDateTime.now().format(Constants.FULL_HOUR);
            if (currentHour.equals(currentName)) {
                //如果文件当前hour相同
                //如果大于,需要将文件重命名
                if (!output.renameTo(new File(targetPrefix + "/" + currentHour + "." + currentIndex))) {
                    throw new RuntimeException("rename error![" + output.getName() + "]");
                }
                currentIndex++;
                output = new File(fullTarget);
                try {
                    stream.close();
                    stream = new FileOutputStream(output, true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                //直接创建新文件
                //先将文件重命名
                if (!output.renameTo(new File(targetPrefix + "/" + currentName + "." + currentIndex))) {
                    throw new RuntimeException("rename error![" + output.getName() + "]");
                }
                output = new File(fullTarget);
                currentName = currentHour;
                currentIndex = 0;
                try {
                    stream.close();
                    stream = new FileOutputStream(output, true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            checkCount = 0;
        }
    }

    public RangersLoggerWriter(final String targetPrefix, final String targetName, int maxSize) {
        this.targetName = targetName;
        this.targetPrefix = targetPrefix;
        File parent = new File(targetPrefix);
        if (!parent.exists()) parent.mkdirs();
        this.currentIndex = setCurrentIndex();
        this.currentIndex++;
        fullTarget = this.targetPrefix + "/" + this.targetName;
        this.output = new File(fullTarget);
        currentName = targetName + "." + LocalDateTime.now().format(Constants.FULL_HOUR);
        try {
            stream = new FileOutputStream(output, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.maxSize = maxSize;
        changeOutputStream();
    }

    public RangersLoggerWriter(final String targetPrefix, final String targetName) {
        this(targetPrefix, targetName, 1024 * 1024 * 200);
    }

//    private int setCurrentIndex() {
//        String current = LocalDateTime.now().format(Constants.FULL_HOUR);
//        String full = targetName + "." + current + ".";
//        int number = 0;
//        for (File f : new File(targetPrefix).listFiles()) {
//            if (f.getName().contains(full)) {
//                String arr = f.getName().replace(full, "");
//                try {
//                    number = Math.max(number, Integer.valueOf(arr));
//                } catch (Exception e) {
//                    continue;
//                }
//            }
//        }
//        return number;
//    }

    private int setCurrentIndex() {
        String current = LocalDateTime.now().format(Constants.FULL_HOUR);
        String full = targetName + "." + current + ".";
        int number = 0;

        File directory = new File(targetPrefix);
        // 检查目录是否存在，如果不存在，则尝试创建它
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (!created) {
                // 如果无法创建目录，记录或抛出异常
                throw new IllegalStateException("无法创建目录：" + targetPrefix);
            }
        }

        // 确保是一个目录
        if (!directory.isDirectory()) {
            throw new IllegalStateException(targetPrefix + " 不是一个有效的目录。");
        }

        // 获取目录下所有文件，如果目录为空或出错，则返回空数组
        File[] files = Optional.ofNullable(directory.listFiles()).orElse(new File[0]);

        // 遍历文件，寻找匹配的文件并更新计数
        for (File f : files) {
            if (f.isFile() && f.getName().contains(full)) {
                String suffix = f.getName().replace(full, "");
                try {
                    // 尝试将文件名中的数字部分转换为整数
                    number = Math.max(number, Integer.parseInt(suffix));
                } catch (NumberFormatException e) {
                    // 如果转换失败，记录或忽略，并继续处理其他文件
                    // 这里可以记录日志或者做其他异常处理
                }
            }
        }
        return number;
    }


    @Override
    public boolean valid(String targetName) {
        return this.targetName.equals(targetName);
    }

    @Override
    public boolean write(String message) {
        if (message == null) return false;
        FileLock lock = null;
        try {
            final FileChannel channel = stream.getChannel();
            lock = channel.lock(0, Integer.MAX_VALUE, false);
            stream.write((message).getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            checkCount++;
            if (lock != null) {
                try {
                    lock.release();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        if (checkCount > 10000) {
            synchronized (stream) {
                changeOutputStream();
            }
        }

        return true;
    }

    @Override
    public void close() {
        try {
            stream.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to close output stream", e);
        }
    }
}
