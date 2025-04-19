package com.ben.smartcv.job.infrastructure.kafka;

import com.ben.smartcv.common.contract.event.JobEvent;
import com.ben.smartcv.common.job.JobCreatedEvent;
import com.ben.smartcv.common.job.JobDeletedEvent;
import com.ben.smartcv.common.util.Constant;
import com.ben.smartcv.common.util.KafkaHelper;
import com.ben.smartcv.common.util.TimeHelper;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class EventPublisher {

    KafkaTemplate<String, JobCreatedEvent> jobCreatedEventTemplate;

    KafkaTemplate<String, JobDeletedEvent> jobDeletedEventTemplate;

    public void send(JobEvent.JobCreated event, String correlationId, String causationId) {
        JobCreatedEvent protoEvent = JobCreatedEvent.newBuilder()
                .setOrganizationName(event.getOrganizationName())
                .setPosition(event.getPosition())
                .setRequirements(event.getRequirements())
                .setExpiredAt(TimeHelper.convertToTimestamp(event.getExpiredAt()))
                .setFromSalary(event.getFromSalary())
                .setToSalary(event.getToSalary())
                .build();

        ProducerRecord<String, JobCreatedEvent> record = new ProducerRecord<>(
                Constant.KAFKA_TOPIC_JOB_EVENT, null, event.getId(), protoEvent,
                KafkaHelper.createHeaders(correlationId, causationId));
        jobCreatedEventTemplate.send(record);
    }

    public void send(JobEvent.JobDeleted event, String correlationId, String causationId) {
        JobDeletedEvent protoEvent = JobDeletedEvent.newBuilder()
                .setJobId(event.getJobId())
                .build();

        ProducerRecord<String, JobDeletedEvent> record = new ProducerRecord<>(
                Constant.KAFKA_TOPIC_JOB_EVENT, null, event.getJobId(), protoEvent,
                KafkaHelper.createHeaders(correlationId, causationId));
        jobDeletedEventTemplate.send(record);
    }
}
