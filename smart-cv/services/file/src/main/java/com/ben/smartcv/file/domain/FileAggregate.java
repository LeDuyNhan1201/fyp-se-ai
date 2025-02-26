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
    String cvId;

    String userId;

    String failedReason;

    @CommandHandler
    public FileAggregate(CvCommand.ApplyCv command) {
        log.info(EventLogger.logCommand("ApplyCv", command.getCvId(),
                Map.of("userId", command.getUserId())));

        if (1 == 0) {
            apply(CvEvent.CvApplicationFailed.builder()
                    .cvId(command.getCvId())
                    .reason("Some reason")
                    .build());
            throw new IllegalStateException("Some exception");

        } else {
            apply(CvEvent.CvApplied.builder()
                    .cvId(command.getCvId())
                    .userId(command.getUserId())
                    .build());
        }
    }

    @EventSourcingHandler
    public void on(CvEvent.CvApplied event) {
        this.userId = event.getUserId();
        this.cvId = event.getCvId();
    }

    @EventSourcingHandler
    public void on(CvEvent.CvApplicationFailed event) {
        this.cvId = event.getCvId();
        this.failedReason = event.getReason();
    }

    @ExceptionHandler(resultType = IllegalStateException.class, payloadType = CvCommand.ApplyCv.class)
    public void handleIllegalStateExceptionsFromIssueCard(Exception exception) {
        log.error("IllegalStateException occurred: {}", exception.getMessage());
    }

}
