package com.datarangers.asynccollector;

import com.datarangers.message.Message;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author hezhiwei.alden@bytedance.com
 * @date 2021/2/4 14:57
 **/
public class RangersCollectorQueue implements CollectorQueue {
    volatile private static CollectorQueue instance = null;

    private BlockingQueue<Message> queue;

    private RangersCollectorQueue(int queueLength) {
        queue = new LinkedBlockingQueue<>(queueLength);
    }

    public static CollectorQueue getInstance(int queueLength) {
        if (instance == null) {
            synchronized (RangersCollectorQueue.class) {
                if (instance == null) {
                    instance = new RangersCollectorQueue(queueLength);
                }
            }
        }
        return instance;
    }

    public static CollectorQueue getInstance(CollectorQueue _queue) {
        if (instance == null) {
            synchronized (RangersCollectorQueue.class) {
                if (instance == null) {
                    instance = _queue;
                }
            }
        }
        return instance;
    }

    @Override
    public List<Message> take() throws InterruptedException {
        return Collections.singletonList(queue.take());
    }

    @Override
    public List<Message> poll(int waitTimeMs) throws InterruptedException {
        Message msg = queue.poll(waitTimeMs, TimeUnit.MILLISECONDS);
        if (msg != null) {
            return Collections.singletonList(msg);
        }
        return null;
    }

    @Override
    public boolean offer(Message t) throws InterruptedException {
        return queue.offer(t);
    }

    @Override
    public int size() {
        return queue.size();
    }
}
