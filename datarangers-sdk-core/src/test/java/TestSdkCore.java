import com.datarangers.collector.AppEventCollector;
import com.datarangers.config.DataRangersException;
import com.datarangers.config.DataRangersSDKConfigProperties;
import com.datarangers.event.Items;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Description: TODO
 * @Author: bytedance
 * @Date 2020/12/2 19:39
 **/
public class TestSdkCore {
    public static void main(String[] args) throws DataRangersException, InterruptedException {
        SecureRandom random = new SecureRandom();
        DataRangersSDKConfigProperties properties = new DataRangersSDKConfigProperties();
        properties.setHeaders(new HashMap<String, String>() {{
            put("Host", "snssdk.vpc.com");
        }});
        List<String> author = new ArrayList<>();
        author.add("abc");
        author.add("def");
        author.add("hij");
        properties.setDomain("http://10.225.129.59");
        List<String> paths = new ArrayList<>();
        paths.add("logs/1/");
        paths.add("logs/2/");
        paths.add("logs/3/");
        paths.add("logs/4/");
        paths.add("logs/5/");
        paths.add("logs/6/");
        properties.setEventFilePaths(paths);
        properties.setEventSaveMaxFileSize(20);
        properties.setQueueSize(1024000);
        properties.setSave(true);
        AppEventCollector eventCollector = new AppEventCollector("app", properties);
        for (int j = 0; j < 100; j++) {
            for (int i = 0; i < 40000; i++) {
                List<Items> items = new ArrayList<>();
                items.add(new Items("1000", "book"));
                items.add(new Items("1002", "book"));
                items.add(new Items("1002", "phone"));
//            eventCollector.itemSet(10000028, "book", items);
                eventCollector.sendEvent("user-" + random.nextInt(500000), 10000028, null, "set_items", new HashMap<String, Object>() {{
                    put("param1", "params");
                    put("param2", items.get(0));
                    put("param3", items.get(1));
                    put("param4", items.get(2));
                }});
                eventCollector.profileSet("user-" + random.nextInt(500000), 10000028, new HashMap<String, Object>() {{
                    put("profile_1", "param_1");
                    put("profile_2", "param_2");
                    put("profile_3", "param_3");
                    put("profile_4", "param_4");
                }});
            }
            Thread.sleep(1000);
        }

    }
}
