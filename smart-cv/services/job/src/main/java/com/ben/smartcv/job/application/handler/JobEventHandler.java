package com.ben.smartcv.job.application.handler;

import com.ben.smartcv.common.component.CommonEventPublisher;
import com.ben.smartcv.common.contract.command.NotificationCommand;
import com.ben.smartcv.common.contract.event.JobEvent;
import com.ben.smartcv.job.domain.entity.Job;
import com.ben.smartcv.job.infrastructure.EventPublisher;
import com.ben.smartcv.job.infrastructure.IJobRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class JobEventHandler {

    EventPublisher kafkaProducer;

    CommonEventPublisher commonEventPublisher;

    IJobRepository jobRepository;

    @EventHandler
    public void on(JobEvent.JobCreated event) {
        Job job = Job.builder()
                .id(event.getJobId())
                .organizationName(event.getOrganizationName())
                .position(event.getPosition())
                .rawText(event.getRequirements())
                .build();

        jobRepository.save(job);
        kafkaProducer.send(event);
    }


    @EventHandler
    public void on(JobEvent.JobProcessed event) {
        kafkaProducer.send(event);
    }

    @EventHandler
    public void on(JobEvent.JobDeleted event) {
        kafkaProducer.send(event);
    }

    @ExceptionHandler(payloadType = JobEvent.JobCreated.class)
    public void handleExceptionForJobCreatedEvent(Exception exception) {
        log.error("Unexpected exception occurred when creating job: {}", exception.getMessage());
        commonEventPublisher.send(NotificationCommand.SendNotification.builder()
                .id(UUID.randomUUID().toString())
                .title("Create job Failed")
                .content("New job is created failed, please try again")
                .build());
    }

}
