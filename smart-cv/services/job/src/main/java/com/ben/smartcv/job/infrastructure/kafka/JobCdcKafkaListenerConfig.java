package com.ben.smartcv.job.infrastructure.kafka;

import com.ben.smartcv.common.infrastructure.kafka.BaseKafkaListenerConfig;
import com.ben.smartcv.common.util.Constant;
import com.ben.smartcv.job.infrastructure.debezium.JobCdcMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;

@Slf4j
@Configuration
public class JobCdcKafkaListenerConfig extends BaseKafkaListenerConfig<JobCdcMessage.Key, JobCdcMessage> {

    public JobCdcKafkaListenerConfig(KafkaProperties kafkaProperties) {
        super(JobCdcMessage.Key.class, JobCdcMessage.class, kafkaProperties);
    }

    @Bean(name = Constant.JOB_CDC_LISTENER_CONTAINER_FACTORY)
    @Override
    public ConcurrentKafkaListenerContainerFactory<JobCdcMessage.Key, JobCdcMessage> listenerContainerFactory() {
        return super.kafkaListenerContainerFactory();
    }

}