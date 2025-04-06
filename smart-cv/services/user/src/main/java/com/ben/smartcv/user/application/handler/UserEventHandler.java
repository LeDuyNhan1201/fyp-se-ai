package com.ben.smartcv.user.application.handler;

import com.ben.smartcv.common.contract.event.UserEvent;
import com.ben.smartcv.common.util.LogHelper;
import com.ben.smartcv.user.infrastructure.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.annotation.MetaDataValue;
import org.springframework.stereotype.Component;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class UserEventHandler {

    EventPublisher kafkaProducer;

    @EventHandler
    public void on(UserEvent.UserRegistered event,
                   @MetaDataValue("correlationId") String correlationId,
                   @MetaDataValue("causationId") String causationId) {
        LogHelper.logMessage(log, "UserRegistered", correlationId, causationId, event);
        kafkaProducer.sendUserRegisteredEvent(event, correlationId, causationId);
    }

}
