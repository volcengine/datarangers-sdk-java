package com.datarangers.config;

import com.datarangers.event.EventV3;
import com.datarangers.event.HeaderV3;
import com.datarangers.event.SaasServerEvent;
import com.datarangers.message.AppMessage;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class RangersJSONConfigTest {
    private final ObjectMapper reader = new ObjectMapper();

    @Test
    public void serializesReportingMessageWithSnakeCaseAndExplicitNames() throws IOException {
        EventV3 event = new EventV3().setEvent("test_event")
                .setSessionId("session-1").setLocalTimeMs(123456789L)
                .setTeaEventIndex(123L).setAbSdkVersion("ab-test")
                .setUserId("ignored-user");
        event.addParams("customCamelCase", "kept");
        HeaderV3 header = new HeaderV3().setAppId(123)
                .setUserUniqueId("user-1").setDeviceId(456L)
                .setSource("server").setPackages("example.package");
        AppMessage message = new AppMessage();
        message.setHeader(header);
        message.addEvent(event);

        JsonNode tree = reader.readTree(RangersJSONConfig.getInstance().toJson(message));
        JsonNode eventTree = tree.path("event_v3").path(0);
        assertEquals("session-1", eventTree.path("session_id").asText());
        assertEquals(123456789L, eventTree.path("local_time_ms").asLong());
        assertEquals(123L, eventTree.path("tea_event_index").asLong());
        assertEquals("ab-test", eventTree.path("ab_sdk_version").asText());
        assertFalse(eventTree.has("sessionId"));
        assertFalse(eventTree.has("user_id"));
        assertFalse(eventTree.has("userId"));
        assertFalse(eventTree.has("event_id"));
        assertEquals("kept", eventTree.path("params").path("customCamelCase").asText());
        assertFalse(eventTree.path("params").has("custom_camel_case"));
        assertEquals("user-1", tree.path("header").path("user_unique_id").asText());
        assertEquals("server", tree.path("header").path("__source").asText());
        assertEquals("example.package", tree.path("header").path("package").asText());
        assertTrue(tree.has("_format_name"));
    }

    @Test
    public void deserializesSnakeCaseAndIgnoresUnknownProperties() throws IOException {
        EventV3 event = RangersJSONConfig.getInstance().fromJson(
                "{\"event\":\"test_event\",\"session_id\":\"session-1\","
                        + "\"local_time_ms\":123456789,\"unknown_field\":true}", EventV3.class);

        assertEquals("test_event", event.getEvent());
        assertEquals("session-1", event.getSessionId());
        assertEquals(Long.valueOf(123456789L), event.getLocalTimeMs());
    }

    @Test
    public void deserializesExplicitHeaderNames() throws IOException {
        HeaderV3 header = RangersJSONConfig.getInstance().fromJson(
                "{\"user_unique_id\":\"user-1\",\"__source\":\"server\","
                        + "\"package\":\"example.package\"}", HeaderV3.class);

        assertEquals("user-1", header.getUserUniqueId());
        assertEquals("server", header.getSource());
        assertEquals("example.package", header.getPackages());
    }

    @Test
    public void preservesSaasParamsAsJsonString() throws IOException {
        EventV3 event = new EventV3().setEvent("test_event").setSessionId("session-1");
        event.addParams("customCamelCase", "kept");

        JsonNode tree = reader.readTree(RangersJSONConfig.getInstance().toJson(new SaasServerEvent(event)));

        assertEquals("session-1", tree.path("session_id").asText());
        assertTrue(tree.path("params").isTextual());
        assertEquals("kept", reader.readTree(tree.path("params").asText()).path("customCamelCase").asText());
    }

    @Test
    public void preservesFieldNamingRulesAndOmitsNullValues() throws IOException {
        RangersJSONConfig config = RangersJSONConfig.getInstance();
        FieldNames fields = new FieldNames();
        fields.userUniqueId = "user-1";
        fields.URLValue = "url";
        fields._user = "user-2";
        fields.explicitName = "exact";

        JsonNode tree = reader.readTree(config.toJson(fields));

        assertEquals(reader.readTree("{\"user_unique_id\":\"user-1\",\"urlvalue\":\"url\","
                + "\"user\":\"user-2\",\"$exactName\":\"exact\"}"), tree);
        FieldNames restored = config.fromJson(tree.toString(), FieldNames.class);
        assertEquals("user-1", restored.userUniqueId);
        assertEquals("url", restored.URLValue);
        assertEquals("user-2", restored._user);
        assertEquals("exact", restored.explicitName);
        assertNull(restored.nullableValue);
    }

    @Test
    public void serializesNull() {
        assertEquals("null", RangersJSONConfig.getInstance().toJson(null));
    }

    @Test(expected = IOException.class)
    public void rejectsMalformedJson() throws IOException {
        RangersJSONConfig.getInstance().fromJson("{", EventV3.class);
    }

    public static class FieldNames {
        public String userUniqueId;
        public String URLValue;
        public String _user;
        public String nullableValue;
        @JsonProperty("$exactName")
        public String explicitName;
    }
}
