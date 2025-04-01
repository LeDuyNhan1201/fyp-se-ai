package com.ben.smartcv.common.infrastructure;

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
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public NewTopic createUserEventTopic() {
        return KafkaHelper.createTopic(
                Constant.KAFKA_TOPIC_USER_EVENT, 6, 1);
    }

    @Bean
    public NewTopic createCvEventTopic() {
        return KafkaHelper.createTopic(
                Constant.KAFKA_TOPIC_CV_EVENT, 6, 1);
    }

//    @Bean
//    public NewTopic createCvCommandTopic() {
//        return KafkaHelper.createTopic(
//                Constant.KAFKA_TOPIC_CV_COMMAND, 6, 1);
//    }

    @Bean
    public NewTopic createJobEventTopic() {
        return KafkaHelper.createTopic(
                Constant.KAFKA_TOPIC_JOB_EVENT, 6, 1);
    }

//    @Bean
//    public NewTopic createJobCommandTopic() {
//        return KafkaHelper.createTopic(
//                Constant.KAFKA_TOPIC_JOB_COMMAND, 6, 1);
//    }

//    @Bean
//    public NewTopic createNotificationCommandTopic() {
//        return KafkaHelper.createTopic(
//                Constant.KAFKA_TOPIC_NOTIFICATION_COMMAND, 6, 1);
//    }
//
//    @Bean
//    public NewTopic createNotificationEventTopic() {
//        return KafkaHelper.createTopic(
//                Constant.KAFKA_TOPIC_NOTIFICATION_EVENT, 6, 1);
//    }

}
