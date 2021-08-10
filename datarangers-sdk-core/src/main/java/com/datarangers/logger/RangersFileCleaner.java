package com.datarangers.logger;

import com.datarangers.asynccollector.CollectorContainer;
import com.datarangers.util.HttpUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Description: 定期清理日志文件
 * @Author: hezhiwei.alden@bytedance.com
 **/
public class RangersFileCleaner implements Runnable {
    public List<String> eventFilePaths;
    public String name;
    public int maxDays;
    public static final String FULL_TIME_PATTERN = "yyyy-MM-dd-HH";

    public RangersFileCleaner(List<String> eventFilePaths, String fileName, int maxDays) {
        this.name = fileName;
        this.maxDays = maxDays;
        this.eventFilePaths = eventFilePaths;
    }

    private void delete(String path) {
        if (path.endsWith("/")) path = path.substring(0, path.length() - 1);
        File parent = new File(path);
        if (!parent.exists()) return;
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, -maxDays);
            SimpleDateFormat sdf = new SimpleDateFormat(FULL_TIME_PATTERN);
            String firstDay = sdf.format(calendar.getTime());
            for (File f : parent.listFiles()) {
                //获取日期
                if (f.getName().contains(name) && !f.getName().equals(name)) {
                    String arr = f.getName().replace(name, "");
                    if (arr.length() > 11) {
                        arr = arr.substring(1, 11);
                        if (firstDay.compareTo(arr) > 0) {
                            new File(path + "/" + f.getName()).delete();
                            HttpUtils.logger.warn("delete " + path + "/" + f.getName());
                            System.out.println("delete " + path + "/" + f.getName());
                        }
                    }
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void delete() {
        for(String eventFilePath : eventFilePaths){
            this.delete(eventFilePath);
        }
    }

    private void release() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -maxDays);
        SimpleDateFormat sdf = new SimpleDateFormat(FULL_TIME_PATTERN);
        String firstDay = sdf.format(calendar.getTime());
        Iterator<Entry<String, ConcurrentHashMap<String, AtomicLong>>> iter = CollectorContainer.SEND_HISTORY
            .entrySet().iterator();
        while (iter.hasNext()) {
            Entry<String, ConcurrentHashMap<String, AtomicLong>> entry = iter.next();
            if (firstDay.compareTo(entry.getKey()) > 0) {
                iter.remove();
            }
        }
    }

    @Override
    public void run() {
        delete();
        release();
    }
}
