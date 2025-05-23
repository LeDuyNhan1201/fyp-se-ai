package com.ben.smartcv.common.infrastructure.kafka;

import com.ben.smartcv.common.util.Constant;
import com.ben.smartcv.common.util.KafkaHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class KafkaConfig {

    @Bean
    public NewTopic createUserEventTopic() {
        return KafkaHelper.createTopic(
                Constant.KAFKA_TOPIC_USER_EVENT, 3, 1);
    }

    @Bean
    public NewTopic createCvEventTopic() {
        return KafkaHelper.createTopic(
                Constant.KAFKA_TOPIC_CV_EVENT, 3, 1);
    }

    @Bean
    public NewTopic createJobEventTopic() {
        return KafkaHelper.createTopic(
                Constant.KAFKA_TOPIC_JOB_EVENT, 3, 1);
    }

    @Bean
    public NewTopic createNotificationEventTopic() {
        return KafkaHelper.createTopic(
                Constant.KAFKA_TOPIC_NOTIFICATION_EVENT, 3, 1);
    }

}
