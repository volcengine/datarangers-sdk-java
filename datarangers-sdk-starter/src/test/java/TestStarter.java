import com.datarangers.collector.EventCollector;
import com.datarangers.event.Items;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Description: TODO
 * @Author: bytedance
 * @Date 2020/12/2 20:14
 **/

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = com.datarangers.config.DataRangersEnableAutoConfiguration.class)
@TestPropertySource("classpath:application.properties")
public class TestStarter {
    @Resource(name = "appEventCollector")
    private EventCollector appEventCollector;

    @Resource(name = "webEventCollector")
    private EventCollector webEventCollector;

    @Resource(name = "mpEventCollector")
    private EventCollector mpEventCollector;

    @Test
    public void testWeb() throws InterruptedException {
        webEventCollector.sendEvent("uuid2", 10000007, null, "test_event_new_java_sdk_test", new HashMap<String, Object>() {{
            put("date_time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
            put("current_time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss")));
            put("zeor", 0);
        }});
        Thread.sleep(10000);
    }

    @Test
    public void testMp() throws InterruptedException {
        appEventCollector.sendEvent("uuid2", 10000000, null, "test_event_new_java_sdk", new HashMap<String, Object>() {{
            put("date_time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
            put("current_time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss")));
        }});
        Thread.sleep(5000);
    }

    @Test
    public void testApp() throws InterruptedException {
        List<String> author = new ArrayList<>();
        author.add("abc");
        author.add("def");
        author.add("hij");
        List<Items> items = new ArrayList<>();
//        items.add(new BookItems("1000", "book").setName("Java").setPrice(100).setPublishDate(LocalDate.now()).setAuthors(author).setCategory("computer"));
//        items.add(new BookItems("1002", "book").setName("PHP").setPrice(100).setPublishDate(LocalDate.now()).setAuthors(author).setCategory("computer"));
//        items.add(new PhoneItems("1002", "phone").setName("Huawei"));
        SecureRandom random = new SecureRandom();
        for (int j = 0; j < 100; j++) {
            for (int i = 0; i < 40000; i++) {
//            eventCollector.itemSet(10000028, "book", items);
                appEventCollector.sendEvent("user-" + random.nextInt(500000), 10000028, null, "set_items", new HashMap<String, Object>() {{
                    put("param1", "params");
                    put("param2", items.get(0));
                    put("param3", items.get(1));
                    put("param4", items.get(2));
                }});
                appEventCollector.profileSet("user-" + random.nextInt(500000), 10000028, new HashMap<String, Object>() {{
                    put("profile_1", "param_1");
                    put("profile_2", "param_2");
                    put("profile_3", "param_3");
                    put("profile_4", "param_4");
                }});
            }
            Thread.sleep(1000);
        }
//        for (int i = 0; i < 10000; i++) {
//            for (int j = 0 + i * 50; j < 50 + i * 50; j++) {
//                mpEventCollector.sendEvent("user" + (j + 900000), 10000007, null, "set_profile", new HashMap<String, Object>() {{
//                    put("sdk", "sdk");
//                }});
//                mpEventCollector.sendEvent("user" + (j + 900000), 10000007,null, ProfileMethod.SET.getMethod(),new HashMap<String, Object>() {{
//                    put("sdk", "sdk");
//                }});
//            }
//        }
//        Thread.sleep(100000);
    }

}
