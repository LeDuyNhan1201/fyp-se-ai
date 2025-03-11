package com.ben.smartcv.curriculum_vitae.domain;

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

    @CommandHandler
    public CvAggregate(CvCommand.ProcessCv command) {
        // 6
        log.info(EventLogger.logCommand("ParseCv", command.getCvId(),
                null));

        apply(CvEvent.CvProcessed.builder()
                .id(command.getId())
                .cvId(command.getCvId())
                .build());
    }

    @CommandHandler
    public CvAggregate(CvCommand.RollbackProcessCv command) {
        log.info(EventLogger.logCommand("RollbackProcessCv", command.getCvId(),
                null));

        apply(CvEvent.CvDeleted.builder()
                .id(command.getId())
                .cvId(command.getCvId())
                .build());
    }

    @EventSourcingHandler
    public void on(CvEvent.CvProcessed event) {
        // 7
        this.id = event.getId();
        this.cvId = event.getCvId();
        this.objectKey = event.getObjectKey();
    }

    @EventSourcingHandler
    public void on(CvEvent.CvDeleted event) {
        // 7
        this.id = event.getId();
        this.cvId = event.getCvId();
    }

    @ExceptionHandler(resultType = Exception.class, payloadType = CvCommand.ApplyCv.class)
    public void handleExceptionForApplyCvCommand(Exception exception) {
        log.error("Unexpected Exception occurred when applied cv: {}", exception.getMessage());
    }

    @ExceptionHandler(resultType = Exception.class, payloadType = CvCommand.ProcessCv.class)
    public void handleExceptionForProcessCvCommand(Exception exception) {
        log.error("IllegalStateException occurred: {}", exception.getMessage());
    }

    @ExceptionHandler(resultType = Exception.class, payloadType = CvCommand.RollbackProcessCv.class)
    public void handleExceptionForRollbackProcessCvCommand(Exception exception) {
        log.error("IllegalStateException occurred: {}", exception.getMessage());
    }

}
