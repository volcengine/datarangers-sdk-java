package com.datarangers.sender.callback;

import com.datarangers.config.RangersJSONConfig;
import com.datarangers.logger.RangersLoggerWriter;
import com.datarangers.sender.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * @Author zhangpeng.spin@bytedance.com
 * @Date 2022-04-24
 */

public class LoggingCallback implements Callback {
  public static final Logger logger = LoggerFactory.getLogger(LoggingCallback.class);


  private static RangersLoggerWriter writer;
  private static final Object lock = new Object();

  public LoggingCallback(final String targetPrefix, final String targetName, int maxSize) {
    initWriter(targetPrefix, targetName, maxSize);
  }

  @Override
  public void onFailed(FailedData failedData) {
    if (failedData.getMessage() == null) {
      return;
    }
    if(!failedData.isListable()){
      writeFailedMessage(failedData.getMessage());
    }else{
      try {
        List list = RangersJSONConfig.getInstance().fromJson(failedData.getMessage(), List.class);
        list.forEach(n-> {
          writeFailedMessage(RangersJSONConfig.getInstance().toJson(n));
        });
      } catch (IOException e) {
        e.printStackTrace();
        logger.error("json error", e);
        writeFailedMessage(failedData.getMessage());
      }
    }

  }

  private void writeFailedMessage(String message) {
    synchronized (writer) {
      writer.write(message + "\n");
    }
  }

  private static void initWriter(final String targetPrefix, final String targetName,
      int maxSize) {
    if (writer == null) {
      synchronized (lock) {
        if (writer == null) {
          writer = RangersLoggerWriter.getInstance(targetPrefix, targetName, maxSize);
        }
      }
    }
  }
}
