package com.ben.smartcv.common.config;

import com.ben.smartcv.common.util.Constant;
import com.ben.smartcv.common.util.KafkaHelper;
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
    public NewTopic createCvCommandTopic() {
        return KafkaHelper.createTopic(
                Constant.KAFKA_TOPIC_CV_COMMAND, 3, 1);
    }

}
