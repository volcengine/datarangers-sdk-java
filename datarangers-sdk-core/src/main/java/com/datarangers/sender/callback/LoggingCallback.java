package com.datarangers.sender.callback;

import com.datarangers.logger.RangersLoggerWriter;
import com.datarangers.sender.Callback;

/**
 * @Author zhangpeng.spin@bytedance.com
 * @Date 2022-04-24
 */

public class LoggingCallback implements Callback {

  private static RangersLoggerWriter writer;
  private static final Object lock = new Object();

  public LoggingCallback(final String targetPrefix, final String targetName, int maxSize) {
    initWriter(targetPrefix, targetName, maxSize);
  }

  @Override
  public void onFailed(FailedData failedData) {
    writeFailedMessage(failedData.getMessage());
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
