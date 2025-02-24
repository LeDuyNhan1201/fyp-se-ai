package com.ben.smartcv.user.application.usecase;

import com.ben.smartcv.user.application.contract.Event;
import com.ben.smartcv.user.infrastructure.EventPublisher;
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
public class UserEventHandler {

    EventPublisher kafkaProducer;

    @EventHandler
    public void on(Event.UserRegistered event) {
        kafkaProducer.sendUserRegisteredEvent(event);
    }

}
