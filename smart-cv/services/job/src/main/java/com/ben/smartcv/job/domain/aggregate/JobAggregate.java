package com.ben.smartcv.job.domain.aggregate;

import com.ben.smartcv.common.contract.command.JobCommand;
import com.ben.smartcv.common.contract.event.JobEvent;
import com.ben.smartcv.common.util.LogHelper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.messaging.MetaData;
import org.axonframework.messaging.annotation.MetaDataValue;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import java.time.Instant;

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

    Instant expiredAt;

    Double fromSalary;

    Double toSalary;

    String requirements;

    @CommandHandler
    public JobAggregate(JobCommand.CreateJob command,
                        @MetaDataValue("correlationId") String correlationId,
                        @MetaDataValue("causationId") String causationId) {
        LogHelper.logMessage(log, "CreateJob", correlationId, causationId, command);
        apply(JobEvent.JobCreated.builder()
                .id(command.getId())
                .organizationName(command.getOrganizationName())
                .position(command.getPosition())
                .fromSalary(command.getFromSalary())
                .toSalary(command.getToSalary())
                .expiredAt(command.getExpiredAt())
                .requirements(command.getRequirements())
                .build(), MetaData.with("correlationId", command.getId()).and("causationId", correlationId));
    }

    @CommandHandler
    public JobAggregate(JobCommand.RollbackCreateJob command,
                        @MetaDataValue("correlationId") String correlationId,
                        @MetaDataValue("causationId") String causationId) {
        LogHelper.logMessage(log, "RollbackCreateJob", correlationId, causationId, command);
        apply(JobEvent.JobDeleted.builder()
                .id(command.getId())
                .jobId(command.getJobId())
                .build(), MetaData.with("correlationId", command.getId()).and("causationId", correlationId));
    }

    @EventSourcingHandler
    public void on(JobEvent.JobCreated event) {
        this.id = event.getId();
        this.organizationName = event.getOrganizationName();
        this.position = event.getPosition();
        this.fromSalary = event.getFromSalary();
        this.toSalary = event.getToSalary();
        this.expiredAt = event.getExpiredAt();
        this.requirements = event.getRequirements();
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

    @ExceptionHandler(resultType = Exception.class, payloadType = JobCommand.RollbackCreateJob.class)
    public void handleExceptionForRollbackProcessJobCommand(Exception exception) {
        log.error("Unexpected exception occurred when rolling back process job: {}", exception.getMessage());
    }

}
