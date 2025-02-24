package com.ben.smartcv.user.infrastructure;

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
    public NewTopic createTopic() {
        return KafkaHelper.createTopic(
                Constant.KAFKA_TOPIC_USER_EVENT, 3, 1);
    }

}
