package com.datarangers.config;

import java.util.Map;

/**
 * @Author zhangpeng.spin@bytedance.com
 * @Date 2022/7/5
 */
public class KafkaConfig {
    private String topic = "sdk_origin_event";
    private String bootstrapServers;
    private Map<String, Object> properties;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        StringBuilder kafkaSb = new StringBuilder();

        kafkaSb.append(" topic:")
                .append(topic)
                .append(" bootstrapServers:")
                .append(bootstrapServers);

        if (properties != null) {
            kafkaSb.append("properties:");
            for (Map.Entry<String, Object> entry:properties.entrySet()) {
                kafkaSb.append(entry.getKey())
                        .append(":")
                        .append(entry.getValue());
            }
        }

        return kafkaSb.toString();
    }
}
