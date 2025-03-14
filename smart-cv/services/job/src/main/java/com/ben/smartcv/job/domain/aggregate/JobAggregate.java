package com.ben.smartcv.job.domain.aggregate;

import com.ben.smartcv.common.contract.command.JobCommand;
import com.ben.smartcv.common.contract.dto.BaseResponse;
import com.ben.smartcv.common.contract.event.JobEvent;
import com.ben.smartcv.common.util.EventLogger;
import com.ben.smartcv.job.application.exception.JobError;
import com.ben.smartcv.job.application.exception.JobHttpException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static lombok.AccessLevel.PRIVATE;
import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@Aggregate
@FieldDefaults(level = PRIVATE)
public class JobAggregate {

    @AggregateIdentifier
    String id;

    String jobId;

    String organizationName;

    String position;

    String requirements;

    @CommandHandler
    public JobAggregate(JobCommand.CreateJob command) {
        apply(JobEvent.JobCreated.builder()
                .id(command.getId())
                .jobId(command.getJobId())
                .organizationName(command.getOrganizationName())
                .position(command.getPosition())
                .requirements(command.getRequirements())
                .build());

        log.info(EventLogger.logCommand("CreateJob", command.getOrganizationName(),
                Map.of("organizationName", command.getOrganizationName())));
    }

    @CommandHandler
    public JobAggregate(JobCommand.ProcessJob command) {
        log.info(EventLogger.logCommand("ProcessJob", command.getJobId(),
                Map.of("jobId", command.getJobId())));

        apply(JobEvent.JobProcessed.builder()
                .id(command.getId())
                .jobId(command.getJobId())
                .build());
    }

    @CommandHandler
    public JobAggregate(JobCommand.RollbackProcessJob command) {
        log.info(EventLogger.logCommand("RollbackProcessJob", command.getJobId(),
                Map.of("jobId", command.getJobId())));

        apply(JobEvent.JobProcessed.builder()
                .id(command.getId())
                .jobId(command.getJobId())
                .build());
    }

    @EventSourcingHandler
    public void on(JobEvent.JobCreated event) {
        this.id = event.getId();
        this.jobId = event.getJobId();
        this.organizationName = event.getOrganizationName();
        this.position = event.getPosition();
        this.requirements = event.getRequirements();
    }

    @EventSourcingHandler
    public void on(JobEvent.JobProcessed event) {
        this.id = event.getId();
        this.jobId = event.getJobId();
    }

    @EventSourcingHandler
    public void on(JobEvent.JobDeleted event) {
        this.id = event.getId();
        this.jobId = event.getJobId();
    }

    @ExceptionHandler(resultType = Exception.class, payloadType = JobCommand.CreateJob.class)
    public void handleExceptionForCreateJobCommand(Exception exception) {
        log.error("Unexpected exception occurred when creating job: {}", exception.getMessage());
    }

    @ExceptionHandler(resultType = Exception.class, payloadType = JobCommand.ProcessJob.class)
    public void handleExceptionForProcessJobCommand(Exception exception) {
        log.error("Unexpected exception occurred when processing job: {}", exception.getMessage());
    }

    @ExceptionHandler(resultType = Exception.class, payloadType = JobCommand.RollbackProcessJob.class)
    public void handleExceptionForRollbackProcessJobCommand(Exception exception) {
        log.error("Unexpected exception occurred when rolling back process job: {}", exception.getMessage());
    }

}
