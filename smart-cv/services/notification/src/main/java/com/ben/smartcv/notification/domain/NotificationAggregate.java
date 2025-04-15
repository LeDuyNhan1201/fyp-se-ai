package com.ben.smartcv.notification.domain;

import com.ben.smartcv.common.contract.command.NotificationCommand;
import com.ben.smartcv.common.contract.event.NotificationEvent;
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
public class NotificationAggregate {

    @AggregateIdentifier
    String id;

    String title;

    String content;

    @CommandHandler
    public NotificationAggregate(NotificationCommand.SendNotification command,
                                 @MetaDataValue("correlationId") String correlationId,
                                 @MetaDataValue("causationId") String causationId) {
        LogHelper.logMessage(log, "SendNotification", correlationId, causationId, command);
        apply(NotificationEvent.NotificationSent.builder()
                .id(command.getId())
                .title(command.getTitle())
                .content(command.getContent())
                .build(), MetaData.with("correlationId", command.getId()).and("causationId", correlationId));
    }

    @EventSourcingHandler
    public void on(NotificationEvent.NotificationSent event) {
        this.id = event.getId();
        this.content = event.getContent();
        this.title = event.getTitle();
    }

    @ExceptionHandler(resultType = Exception.class, payloadType = NotificationCommand.SendNotification.class)
    public void handleExceptionForNotificationSentEvent(Exception exception) {
        log.error("Unexpected Exception occurred when send notification: {}", exception.getMessage());
    }

}
