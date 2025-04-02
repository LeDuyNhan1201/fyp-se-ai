package com.ben.smartcv.job.application.handler;

import com.ben.smartcv.common.contract.command.NotificationCommand;
import com.ben.smartcv.common.contract.event.JobEvent;
import com.ben.smartcv.common.job.ExtractedJobData;
import com.ben.smartcv.job.application.usecase.IMasterJobWriteSideUseCase;
import com.ben.smartcv.job.domain.entity.MasterJob;
import com.ben.smartcv.job.infrastructure.kafka.EventPublisher;
import com.ben.smartcv.job.infrastructure.grpc.GrpcClientJobProcessor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
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

    EventPublisher eventPublisher;

    CommandGateway commandGateway;

    GrpcClientJobProcessor grpcClient;

    IMasterJobWriteSideUseCase useCase;

    @EventHandler
    public void on(JobEvent.JobCreated event) {
        try {
            ExtractedJobData extractedJobData = grpcClient.callExtractData(event);

            MasterJob masterJob = MasterJob.builder()
                    .organizationName(event.getOrganizationName())
                    .position(event.getPosition())
                    .expiredAt(event.getExpiredAt())
                    .fromSalary(event.getFromSalary())
                    .toSalary(event.getToSalary())
                    .rawText(event.getRequirements())

                    .email(extractedJobData.getEmail())
                    .phone(extractedJobData.getPhone())
                    .educations(extractedJobData.getEducationsList())
                    .skills(extractedJobData.getSkillsList())
                    .experiences(extractedJobData.getExperiencesList())
                    .build();
            useCase.create(masterJob);

        } catch (Exception e) {
            log.error("Job processing failed: {}", e.getMessage());
            sendFailureNotification();
        }
        eventPublisher.send(event);
    }

    private void sendFailureNotification() {
        commandGateway.sendAndWait(NotificationCommand.SendNotification.builder()
                .id(UUID.randomUUID().toString())
                .title("Create job failed")
                .content("Job creating failed")
                .build());
    }

    @EventHandler
    public void on(JobEvent.JobDeleted event) {
        useCase.delete(event.getJobId());
        eventPublisher.send(event);
    }

    @ExceptionHandler(payloadType = JobEvent.JobCreated.class)
    public void handleExceptionForJobDeletedEvent(Exception exception) {
        log.error("Unexpected exception occurred when creating job: {}", exception.getMessage());
    }

    @ExceptionHandler
    public void handleExceptionForJobDeletedEvent(JobEvent.JobDeleted event, Exception exception) {
        log.error("Unexpected exception occurred when deleting job {}: {}", event.getJobId(), exception.getMessage());
    }

}
