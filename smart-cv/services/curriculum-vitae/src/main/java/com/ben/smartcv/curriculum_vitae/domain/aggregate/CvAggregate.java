package com.ben.smartcv.curriculum_vitae.domain.aggregate;

import com.ben.smartcv.common.contract.command.CvCommand;
import com.ben.smartcv.common.contract.event.CvEvent;
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

import static lombok.AccessLevel.PRIVATE;
import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@Aggregate
@FieldDefaults(level = PRIVATE)
public class CvAggregate {

    @AggregateIdentifier
    String id;

    String cvId;

    String objectKey;

    String jobId;

    String createdBy;

    String title;

    String content;

    String receiverId;

    @CommandHandler
    public CvAggregate(CvCommand.ProcessCv command,
                       @MetaDataValue("correlationId") String correlationId,
                       @MetaDataValue("causationId") String causationId) {
        // 6
        LogHelper.logMessage(log, "6|ProcessCv", correlationId, causationId, command);
        apply(CvEvent.CvProcessed.builder()
                .id(command.getId())
                .objectKey(command.getObjectKey())
                .jobId(command.getJobId())
                .createdBy(command.getCreatedBy())
                .build(), MetaData.with("correlationId", command.getId()).and("causationId", correlationId));
    }

    @CommandHandler
    public CvAggregate(CvCommand.RollbackProcessCv command,
                       @MetaDataValue("correlationId") String correlationId,
                       @MetaDataValue("causationId") String causationId) {
        // 2
        LogHelper.logMessage(log, "2|RollbackProcessCv", correlationId, causationId, command);
        apply(CvEvent.CvDeleted.builder()
                .id(command.getId())
                .objectKey(command.getObjectKey())
                .build(), MetaData.with("correlationId", command.getId()).and("causationId", correlationId));
    }

    @CommandHandler
    public CvAggregate(CvCommand.ApproveCv command,
                       @MetaDataValue("correlationId") String correlationId,
                       @MetaDataValue("causationId") String causationId) {
        LogHelper.logMessage(log, "ApproveCv", correlationId, causationId, command);
        apply(CvEvent.CvApproved.builder()
                .id(command.getId())
                .title(command.getTitle())
                .content(command.getContent())
                .jobId(command.getJobId())
                .receiverId(command.getReceiverId())
                .cvId(command.getCvId())
                .build(), MetaData.with("correlationId", command.getId()).and("causationId", correlationId));
    }

    @CommandHandler
    public CvAggregate(CvCommand.RollbackApproveCv command,
                       @MetaDataValue("correlationId") String correlationId,
                       @MetaDataValue("causationId") String causationId) {
        LogHelper.logMessage(log, "ApproveCv", correlationId, causationId, command);
        apply(CvEvent.CvRenewed.builder()
                .id(command.getId())
                .cvId(command.getCvId())
                .build(), MetaData.with("correlationId", command.getId()).and("causationId", correlationId));
    }

    @EventSourcingHandler
    public void on(CvEvent.CvProcessed event) {
        // 7
        this.id = event.getId();
        this.objectKey = event.getObjectKey();
        this.jobId = event.getJobId();
        this.createdBy = event.getCreatedBy();
    }

    @EventSourcingHandler
    public void on(CvEvent.CvDeleted event) {
        // 3
        this.id = event.getId();
        this.objectKey = event.getObjectKey();
    }

    @EventSourcingHandler
    public void on(CvEvent.CvApproved event) {
        this.id = event.getId();
        this.title = event.getTitle();
        this.content = event.getContent();
        this.jobId = event.getJobId();
        this.receiverId = event.getReceiverId();
        this.cvId = event.getCvId();
    }

    @EventSourcingHandler
    public void on(CvEvent.CvRenewed event) {
        this.id = event.getId();
        this.cvId = event.getCvId();
    }

    @ExceptionHandler(resultType = Exception.class, payloadType = CvCommand.ApplyCv.class)
    public void handleExceptionForApplyCvCommand(Exception exception) {
        log.error("Unexpected Exception occurred when applied cv: {}", exception.getMessage());
    }

    @ExceptionHandler(resultType = Exception.class, payloadType = CvCommand.ProcessCv.class)
    public void handleExceptionForProcessCvCommand(Exception exception) {
        log.error("Unexpected Exception occurred when processed cv: {}", exception.getMessage());
    }

    @ExceptionHandler(resultType = Exception.class, payloadType = CvCommand.RollbackProcessCv.class)
    public void handleExceptionForRollbackProcessCvCommand(Exception exception) {
        log.error("Unexpected Exception occurred when rolled back process cv: {}", exception.getMessage());
    }

    @ExceptionHandler(resultType = Exception.class, payloadType = CvCommand.ApproveCv.class)
    public void handleExceptionForRollbackApproveCvCommand(Exception exception) {
        log.error("Unexpected Exception occurred when approve cv: {}", exception.getMessage());
    }

    @ExceptionHandler(resultType = Exception.class, payloadType = CvCommand.RollbackApproveCv.class)
    public void handleExceptionForRollbackRollbackApproveCvCommand(Exception exception) {
        log.error("Unexpected Exception occurred when rolled back approve cv: {}", exception.getMessage());
    }

}
