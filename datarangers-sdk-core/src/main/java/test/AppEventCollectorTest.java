package test;

import com.datarangers.collector.AppEventCollector;
import com.datarangers.config.DataRangersSDKConfigProperties;
import com.datarangers.config.KafkaConfig;
import com.datarangers.config.SdkMode;
import org.junit.Test;

/**
 * @author qifeng.64343747@bytedance.com
 * @date 2022-08-18
 */
public class AppEventCollectorTest {

    @Test
    public void initAppEventCollectorTest() {
        //kafka
        DataRangersSDKConfigProperties dataRangersSDKConfigProperties = new DataRangersSDKConfigProperties();
        dataRangersSDKConfigProperties.setMode(SdkMode.KAFKA);
        KafkaConfig kafkaConfig = new KafkaConfig();
        kafkaConfig.setBootstrapServers("servers");
        dataRangersSDKConfigProperties.setKafka(kafkaConfig);
        AppEventCollector appEventCollector = new AppEventCollector("app", dataRangersSDKConfigProperties, null);

        // http
        dataRangersSDKConfigProperties.setMode(SdkMode.HTTP);
        appEventCollector = new AppEventCollector("app", dataRangersSDKConfigProperties, null);
        //file
        dataRangersSDKConfigProperties.setMode(SdkMode.FILE);
        appEventCollector = new AppEventCollector("app", dataRangersSDKConfigProperties, null);

    }

    @Test
    public void sendEvent() {

    }
}
