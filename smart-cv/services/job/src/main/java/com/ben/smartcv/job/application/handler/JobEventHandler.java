package com.ben.smartcv.job.application.handler;

import com.ben.smartcv.common.contract.command.NotificationCommand;
import com.ben.smartcv.common.contract.event.JobEvent;
import com.ben.smartcv.common.job.ExtractedJobData;
import com.ben.smartcv.common.util.LogHelper;
import com.ben.smartcv.job.application.usecase.IMasterJobWriteSideUseCase;
import com.ben.smartcv.job.domain.entity.MasterJob;
import com.ben.smartcv.job.infrastructure.kafka.EventPublisher;
import com.ben.smartcv.job.infrastructure.grpc.GrpcClientJobProcessor;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.annotation.MetaDataValue;
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
    public void on(JobEvent.JobCreated event,
                   @MetaDataValue("correlationId") String correlationId,
                   @MetaDataValue("causationId") String causationId) {
        LogHelper.logMessage(log, "JobCreated", correlationId, causationId, event);
        try {
            ExtractedJobData extractedJobData = grpcClient.callExtractData(event);

            MasterJob masterJob = MasterJob.builder()
                    .organizationName(event.getOrganizationName())
                    .position(event.getPosition())
                    .expiredAt(event.getExpiredAt())
                    .fromSalary(event.getFromSalary())
                    .toSalary(event.getToSalary())
                    .requirements(event.getRequirements())

                    .email(extractedJobData.getEmail())
                    .phone(extractedJobData.getPhone())
                    .educations(extractedJobData.getEducationsList())
                    .skills(extractedJobData.getSkillsList())
                    .experiences(extractedJobData.getExperiencesList())
                    .build();
            useCase.create(masterJob);

        } catch (Exception e) {
            log.error("Job processing failed: {}", e.getMessage());
            String reason = "Notify.Content.CreateFailed|CV";
            if (e instanceof StatusRuntimeException) {
                Status status = ((StatusRuntimeException) e).getStatus();
                if (status.getCode() == Status.Code.INVALID_ARGUMENT) {
                    log.error("Invalid argument: {}", status.getDescription());
                    reason = "Notify.Content.InvalidRequirements|CV";
                } else {
                    log.error("Unexpected error: {}", status.getDescription());
                }
            }
            sendFailureNotification(reason);
        }
        eventPublisher.send(event, correlationId, causationId);
    }

    @EventHandler
    public void on(JobEvent.JobDeleted event,
                   @MetaDataValue("correlationId") String correlationId,
                   @MetaDataValue("causationId") String causationId) {
        LogHelper.logMessage(log, "JobDeleted", correlationId, causationId, event);
        try {
            useCase.delete(event.getJobId());
            eventPublisher.send(event, correlationId, causationId);
        } catch (Exception exception){
            log.error("Cannot delete job {}: {}", event.getJobId(), exception.getMessage());
        }
    }

    @ExceptionHandler(payloadType = JobEvent.JobCreated.class)
    public void handleExceptionForJobDeletedEvent(Exception exception) {
        log.error("Unexpected exception occurred when creating job: {}", exception.getMessage());
    }

    @ExceptionHandler
    public void handleExceptionForJobDeletedEvent(JobEvent.JobDeleted event, Exception exception) {
        log.error("Unexpected exception occurred when deleting job {}: {}", event.getJobId(), exception.getMessage());
    }

    private void sendFailureNotification(String reason) {
        commandGateway.sendAndWait(NotificationCommand.SendNotification.builder()
                .id(UUID.randomUUID().toString())
                .title("Notify.Title.CreateFailed|CV")
                .content(reason)
                .build());
    }

}
