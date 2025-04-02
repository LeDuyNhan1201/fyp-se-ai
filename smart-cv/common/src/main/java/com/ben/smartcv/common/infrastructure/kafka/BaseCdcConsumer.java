package com.ben.smartcv.common.infrastructure.kafka;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.MessageHeaders;

/**
 * Base class for CDC (Change Data Capture) Kafka consumers.
 * Provides common methods for processing messages and handling Dead Letter Topic (DLT) events.
 *
 * @param <K> Type of the message payload.
 */
@Slf4j
public abstract class BaseCdcConsumer<K, V> {

    public static final String RECEIVED_MESSAGE_HEADERS = "## Received message - headers: {}";
    public static final String PROCESSING_RECORD_KEY_VALUE = "## Processing record - Key: {} | Value: {}";
    public static final String RECORD_PROCESSED_SUCCESSFULLY_KEY = "## Record processed successfully - Key: {} \n";

    protected void processMessage(V record, MessageHeaders headers, Consumer<V> consumer) {
        log.debug(RECEIVED_MESSAGE_HEADERS, headers);
        log.debug(PROCESSING_RECORD_KEY_VALUE, headers.get(KafkaHeaders.RECEIVED_KEY), record);
        consumer.accept(record);
        log.debug(RECORD_PROCESSED_SUCCESSFULLY_KEY, headers.get(KafkaHeaders.RECEIVED_KEY));
    }

    protected void processMessage(K key, V value, MessageHeaders headers, BiConsumer<K, V> consumer) {
        log.debug(RECEIVED_MESSAGE_HEADERS, headers);
        log.debug(PROCESSING_RECORD_KEY_VALUE, key, value);
        consumer.accept(key, value);
        log.debug(RECORD_PROCESSED_SUCCESSFULLY_KEY, key);
    }

}