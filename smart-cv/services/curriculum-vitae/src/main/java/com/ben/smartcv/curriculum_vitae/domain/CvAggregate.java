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
    String cvId;

    String failedReason;

    @CommandHandler
    public CvAggregate(CvCommand.ParseCv command) {
        // 6
        log.info(EventLogger.logCommand("ParseCv", command.getCvId(),
                null));

        apply(CvEvent.CvParsed.builder()
                .cvId(command.getCvId())
                .build());
    }

    @EventSourcingHandler
    public void on(CvEvent.CvParsed event) {
        // 7
        this.cvId = event.getCvId();
    }

    @ExceptionHandler(resultType = IllegalStateException.class, payloadType = CvCommand.ParseCv.class)
    public void handleIllegalStateExceptionsFromIssueCard(Exception exception) {
        log.error("IllegalStateException occurred: {}", exception.getMessage());
    }

}
