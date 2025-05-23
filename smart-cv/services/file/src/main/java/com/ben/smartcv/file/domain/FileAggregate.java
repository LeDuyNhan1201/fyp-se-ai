package com.ben.smartcv.file.domain;

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
public class FileAggregate {

    @AggregateIdentifier
    String id;

    String objectKey;

    String jobId;

    String createdBy;

    @CommandHandler
    public FileAggregate(CvCommand.ApplyCv command,
                         @MetaDataValue("correlationId") String correlationId,
                         @MetaDataValue("causationId") String causationId) {
        // 1
        LogHelper.logMessage(log, "1|ApplyCv", correlationId, causationId, command);
        apply(CvEvent.CvApplied.builder()
                .id(command.getId())
                .objectKey(command.getObjectKey())
                .jobId(command.getJobId())
                .createdBy(command.getCreatedBy())
                .build(), MetaData.with("correlationId", command.getId()).and("causationId", correlationId));
    }

    @CommandHandler
    public FileAggregate(CvCommand.DeleteCvFile command,
                         @MetaDataValue("correlationId") String correlationId,
                         @MetaDataValue("causationId") String causationId) {
        // 7
        LogHelper.logMessage(log, "7|DeleteCvFile", command.getId(), causationId, command);
        apply(CvEvent.CvFileDeleted.builder()
                .id(command.getId())
                .objectKey(command.getObjectKey())
                .build(), MetaData.with("correlationId", command.getId()).and("causationId", correlationId));
    }

    @EventSourcingHandler
    public void on(CvEvent.CvApplied event) {
        // 2
        this.id = event.getId();
        this.objectKey = event.getObjectKey();
        this.jobId = event.getJobId();
        this.createdBy = event.getCreatedBy();
    }

    @EventSourcingHandler
    public void on(CvEvent.CvFileDeleted event) {
        // 8
        this.id = event.getId();
        this.objectKey = event.getObjectKey();
    }

    @ExceptionHandler(resultType = Exception.class, payloadType = CvCommand.ApplyCv.class)
    public void handleExceptionForApplyCvCommand(Exception exception) {
        log.error("Unexpected Exception occurred when applying cv: {}", exception.getMessage());
    }

    @ExceptionHandler(resultType = Exception.class, payloadType = CvCommand.DeleteCvFile.class)
    public void handleExceptionForDeleteCvFileCommand(Exception exception) {
        log.error("Unexpected Exception occurred when deleting cv file: {}", exception.getMessage());
    }

}
