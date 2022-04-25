/*
 * Copyright 2020 Beijing Volcano Engine Technology Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.datarangers.asynccollector;

import com.datarangers.config.DataRangersSDKConfigProperties;
import com.datarangers.config.EventConfig;
import com.datarangers.config.RangersJSONConfig;
import com.datarangers.logger.RangersLoggerWriterPool;
import com.datarangers.message.AppMessage;
import com.datarangers.message.Message;
import com.datarangers.sender.MessageSenderFactory;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Consumer implements Runnable {

  private static final Logger logger = LoggerFactory.getLogger(Consumer.class);
  private static RangersLoggerWriterPool pool;
  private CollectorContainer collectorContainer;
  private DataRangersSDKConfigProperties sdkConfigProperties;

  public Consumer(CollectorContainer collectorContainer,
      DataRangersSDKConfigProperties sdkConfigProperties) {
    this.collectorContainer = collectorContainer;
    this.sdkConfigProperties = sdkConfigProperties;
  }

  public static void setWriterPool(final List<String> targetPrefixes, String targetNames,
      int maxSize) {
    if (pool == null) {
      synchronized (Consumer.class) {
        if (pool == null) {
          pool = RangersLoggerWriterPool.getInstance(targetPrefixes, targetNames, maxSize);
        }
      }
    }
  }

  private void send() throws Exception {
    while (true) {
      try {
        List<Message> messages = collectorContainer.consume();
        if (messages != null) {
          messages.forEach(message -> {
            doSend(message);
          });
        }
      } catch (Throwable e) {
        e.printStackTrace();
        logger.error("consumer send error", e);
      }
    }
  }

  private void write() throws Exception {
    while (true) {
      try {
        List<Message> messages = collectorContainer.consume();
        if (messages != null) {
          messages.forEach(message -> {
            doWrite(message);
          });
        }
      } catch (Throwable e) {
        logger.error("consumer write error", e);
      }

    }
  }

  @Override
  public void run() {
    try {
      if (EventConfig.saveFlag) {
        write();
      } else {
        send();
      }
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("consumer run error", e);
    }
  }

  public void flush() {
    try {
      System.out.println("flush message start");
      logger.info("flush message start");
      int count = 0;
      if (collectorContainer.getMessageQueue() != null) {
        Message message = collectorContainer.getMessageQueue().poll();
        while (message != null) {
          count++;
          message = collectorContainer.handleMessage(message);
          if (EventConfig.saveFlag) {
            doWrite(message);
          } else {
            doSend(message);
          }
          message = collectorContainer.getMessageQueue().poll();
        }
      }

      logger.info("flush message success. size: {}", count);
      System.out.println("flush message success. size: " + count);
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("flush message error", e);
    }
  }

  public void flush(Message message) {
    message = collectorContainer.handleMessage(message);
    if (EventConfig.saveFlag) {
      doWrite(message);
    } else {
      doSend(message);
    }
  }

  private void doSend(Message message) {
    MessageSenderFactory.getMessageSender(message)
        .send(message, this.sdkConfigProperties);
  }

  private void doWrite(Message message) {
    AppMessage appMessage = message.getAppMessage();
    pool.getWriter(appMessage.getUserUniqueId())
        .write(RangersJSONConfig.getInstance().toJson(appMessage) + "\n");
  }
}
