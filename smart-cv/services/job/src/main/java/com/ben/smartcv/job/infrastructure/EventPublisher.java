package com.ben.smartcv.job.infrastructure;

import com.ben.smartcv.common.contract.event.JobEvent;
import com.ben.smartcv.common.job.JobCreatedEvent;
import com.ben.smartcv.common.job.JobDeletedEvent;
import com.ben.smartcv.common.job.JobProcessedEvent;
import com.ben.smartcv.common.util.Constant;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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
                .build();

        jobCreatedEventTemplate.send(
                Constant.KAFKA_TOPIC_JOB_EVENT,
                event.getJobId(),
                protoEvent
        );
    }

    public void send(JobEvent.JobProcessed event) {
        JobProcessedEvent protoEvent = JobProcessedEvent.newBuilder()
                .setJobId(event.getJobId())
                .build();

        jobProcessedEventTemplate.send(
                Constant.KAFKA_TOPIC_JOB_EVENT,
                event.getJobId(),
                protoEvent
        );
    }

    public void send(JobEvent.JobDeleted event) {
        JobDeletedEvent protoEvent = JobDeletedEvent.newBuilder()
                .setJobId(event.getJobId())
                .build();

        jobDeletedEventTemplate.send(
                Constant.KAFKA_TOPIC_JOB_EVENT,
                event.getJobId(),
                protoEvent
        );
    }
}
