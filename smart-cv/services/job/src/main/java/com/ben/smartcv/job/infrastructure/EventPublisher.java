package com.ben.smartcv.job.infrastructure;

import com.ben.smartcv.common.contract.event.JobEvent;
import com.ben.smartcv.common.job.JobCreatedEvent;
import com.ben.smartcv.common.job.JobDeletedEvent;
import com.ben.smartcv.common.job.JobProcessedEvent;
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

    KafkaTemplate<String, JobProcessedEvent> jobProcessedEventTemplate;

    KafkaTemplate<String, JobDeletedEvent> jobDeletedEventTemplate;

    public void send(JobEvent.JobCreated event) {
        JobCreatedEvent protoEvent = JobCreatedEvent.newBuilder()
                .setJobId(event.getJobId())
                .setOrganizationName(event.getOrganizationName())
                .setPosition(event.getPosition())
                .setRequirements(event.getRequirements())
                .setExpiredAt(TimeHelper.convertToTimestamp(event.getExpiredAt()))
                .setFromSalary(event.getFromSalary())
                .setToSalary(event.getToSalary())
                .build();

        ProducerRecord<String, JobCreatedEvent> record = new ProducerRecord<>(
                Constant.KAFKA_TOPIC_JOB_EVENT, null, event.getJobId(), protoEvent,
                KafkaHelper.createHeaders(event.getId(), event.getId()));
        jobCreatedEventTemplate.send(record);
    }

    public void send(JobEvent.JobProcessed event) {
        JobProcessedEvent protoEvent = JobProcessedEvent.newBuilder()
                .setJobId(event.getJobId())
                .build();

        ProducerRecord<String, JobProcessedEvent> record = new ProducerRecord<>(
                Constant.KAFKA_TOPIC_JOB_EVENT, null, event.getJobId(), protoEvent,
                KafkaHelper.createHeaders(event.getId(), event.getId()));
        jobProcessedEventTemplate.send(record);
    }

    public void send(JobEvent.JobDeleted event) {
        JobDeletedEvent protoEvent = JobDeletedEvent.newBuilder()
                .setJobId(event.getJobId())
                .build();

        ProducerRecord<String, JobDeletedEvent> record = new ProducerRecord<>(
                Constant.KAFKA_TOPIC_JOB_EVENT, null, event.getJobId(), protoEvent,
                KafkaHelper.createHeaders(event.getId(), event.getId()));
        jobDeletedEventTemplate.send(record);
    }
}
