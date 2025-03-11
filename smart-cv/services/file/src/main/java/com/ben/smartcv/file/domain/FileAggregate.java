package com.ben.smartcv.file.domain;

import com.ben.smartcv.common.contract.command.CvCommand;
import com.ben.smartcv.common.contract.event.CvEvent;
import com.ben.smartcv.common.util.EventLogger;
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

import java.util.Map;

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

    String cvId;

    String objectKey;

    @CommandHandler
    public FileAggregate(CvCommand.ApplyCv command) {
        // 1
        log.info(EventLogger.logCommand("ApplyCv", command.getCvId(),
                Map.of("fileName", command.getFileName())));

        apply(CvEvent.CvApplied.builder()
                .id(command.getId())
                .cvId(command.getCvId())
                .fileName(command.getFileName())
                .build());
    }

    @CommandHandler
    public FileAggregate(CvCommand.DeleteCvFile command) {
        // 7
        log.info(EventLogger.logCommand("DeleteCvFile", command.getCvId(),
                Map.of("objectKey", command.getObjectKey())));
        apply(CvEvent.CvFileDeleted.builder()
                .id(command.getId())
                .cvId(command.getCvId())
                .objectKey(command.getObjectKey())
                .build());
    }

    @EventSourcingHandler
    public void on(CvEvent.CvApplied event) {
        // 2
        this.id = event.getId();
        this.objectKey = event.getFileName();
        this.cvId = event.getCvId();
    }

    @EventSourcingHandler
    public void on(CvEvent.CvFileDeleted event) {
        // 8
        this.id = event.getId();
        this.objectKey = event.getObjectKey();
        this.cvId = event.getCvId();
    }

    @ExceptionHandler(resultType = Exception.class, payloadType = CvCommand.ApplyCv.class)
    public void handleExceptionForApplyCvCommand(Exception exception) {
        log.error("IllegalStateException occurred: {}", exception.getMessage());
    }

    @ExceptionHandler(resultType = Exception.class, payloadType = CvCommand.DeleteCvFile.class)
    public void handleExceptionForDeleteCvFileCommand(Exception exception) {
        log.error("IllegalStateException occurred: {}", exception.getMessage());
    }

}
