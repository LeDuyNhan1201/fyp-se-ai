package com.ben.smartcv.common.util;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.config.TopicBuilder;

import java.nio.charset.StandardCharsets;
import java.util.List;

public final class KafkaHelper {

    public static NewTopic createTopic(String topicName, int partitions, int replicas) {
        return TopicBuilder.name(topicName)
                .partitions(partitions)
                .replicas(replicas)
                .build();
    }

    public static List<Header> createHeaders(String correlationId, String causationId) {
        return List.of(
                new RecordHeader("correlationId", correlationId.getBytes(StandardCharsets.UTF_8)),
                new RecordHeader("causationId", causationId.getBytes(StandardCharsets.UTF_8))
        );
    }

}
