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

    String userId;

    String failedReason;

    @CommandHandler
    public FileAggregate(CvCommand.ApplyCv command) {
        // 1
        log.info(EventLogger.logCommand("ApplyCv", command.getCvId(),
                Map.of("userId", command.getUserId())));

        apply(CvEvent.CvApplied.builder()
                .id(command.getId())
                .cvId(command.getCvId())
                .userId(command.getUserId())
                .build());
    }

    @EventSourcingHandler
    public void on(CvEvent.CvApplied event) {
        // 2
        this.id = event.getId();
        this.userId = event.getUserId();
        this.cvId = event.getCvId();
    }

    @ExceptionHandler(resultType = IllegalStateException.class, payloadType = CvCommand.ApplyCv.class)
    public void handleIllegalStateExceptionsFromIssueCard(Exception exception) {
        log.error("IllegalStateException occurred: {}", exception.getMessage());
    }

}
