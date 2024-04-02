package com.datarangers.asynccollector;

import com.datarangers.collector.Collector;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author zhangpeng.spin
 */
public class CollectorCounterTest {
    @Test
    public void testInit(){
        Collector.collectorContainer = new CollectorContainer(RangersCollectorQueue.getInstance(10));
        CollectorCounter collectorCounter = new CollectorCounter("logs2");
        collectorCounter.run();
    }
}