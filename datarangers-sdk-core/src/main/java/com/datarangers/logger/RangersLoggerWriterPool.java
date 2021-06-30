package com.datarangers.logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class RangersLoggerWriterPool {
    private static RangersLoggerWriterPool POOL = null;
    private static final List<RangersLoggerWriter> WRITERS_POOL = new ArrayList<>();
    private int size = 0;
    private int count = 0;

    public void setInstance(final List<String> targetPrefixes, String targetNames, int maxSize) {
        synchronized (WRITERS_POOL) {
            if (WRITERS_POOL.size() == 0) {
                HashSet<String> prefixSet = new HashSet<>(targetPrefixes);
                for (String prefix : prefixSet) {
                    WRITERS_POOL.add(RangersLoggerWriter.getInstance(prefix, targetNames, maxSize));
                }
                size = WRITERS_POOL.size();
            }
        }
    }

    private RangersLoggerWriterPool() {
    }

    public static RangersLoggerWriterPool getInstance(final List<String> targetPrefixes, String targetNames, int maxSize) {
        if (POOL == null) {
            synchronized (RangersLoggerWriterPool.class) {
                if (POOL == null) {
                    POOL = new RangersLoggerWriterPool();
                    POOL.setInstance(targetPrefixes, targetNames, maxSize);
                }
            }
        }
        return POOL;
    }

    public RangersLoggerWriter getWriter() {
        if (WRITERS_POOL.size() == 1) return getOneWriter();
        if (WRITERS_POOL.size() > 0) {
            count = (count + 1) % WRITERS_POOL.size();
            return WRITERS_POOL.get(count);
        }
        return null;
    }

    public RangersLoggerWriter getWriter(String uuid) {
        if (WRITERS_POOL.size() == 1) return getOneWriter();
        if (WRITERS_POOL.size() > 0) {
            if (uuid == null) {
                return WRITERS_POOL.get(0);
            }
            return WRITERS_POOL.get(Math.abs(uuid.hashCode()) % size);
        }
        return null;
    }

    public RangersLoggerWriter getOneWriter() {
        return WRITERS_POOL.get(0);
    }
}
