package com.ben.smartcv.notification.application.handler;

import com.ben.smartcv.common.contract.event.NotificationEvent;
import com.ben.smartcv.notification.infrastructure.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class NotificationEventHandler {

    EventPublisher kafkaProducer;

    @EventHandler
    public void on(NotificationEvent.NotificationSent event) {
        kafkaProducer.send(event);
    }

}
