package com.ben.smartcv.notification.domain;

import com.ben.smartcv.common.contract.command.NotificationCommand;
import com.ben.smartcv.common.contract.event.CvEvent;
import com.ben.smartcv.common.contract.event.NotificationEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.messaging.MetaData;
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
    public NotificationAggregate(NotificationCommand.SendNotification command) {
        apply(NotificationEvent.NotificationSent.builder()
                .title(command.getTitle())
                .content(command.getContent())
                .build(), MetaData.with("key", "123"));
    }

    @EventSourcingHandler
    public void on(NotificationEvent.NotificationSent event) {
        this.id = event.getId();
        this.content = event.getContent();
        this.title = event.getTitle();
    }

    @ExceptionHandler(resultType = Exception.class, payloadType = NotificationCommand.SendNotification.class)
    public void handleNotificationSentException(Exception exception) {
        log.error("Unexpected Exception occurred when send notification: {}", exception.getMessage());
    }

}
