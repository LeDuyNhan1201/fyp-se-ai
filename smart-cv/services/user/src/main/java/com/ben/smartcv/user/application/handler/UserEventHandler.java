package com.ben.smartcv.user.application.handler;

import com.ben.smartcv.common.contract.command.NotificationCommand;
import com.ben.smartcv.common.contract.event.UserEvent;
import com.ben.smartcv.common.util.LogHelper;
import com.ben.smartcv.user.application.usecase.IAuthenticationUseCase;
import com.ben.smartcv.user.infrastructure.kafka.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.annotation.MetaDataValue;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class UserEventHandler {

    EventPublisher kafkaProducer;

    IAuthenticationUseCase authenticationUseCase;

    CommandGateway commandGateway;

    @EventHandler
    public void on(UserEvent.SignedUp event,
                   @MetaDataValue("correlationId") String correlationId,
                   @MetaDataValue("causationId") String causationId) {
        LogHelper.logMessage(log, "SignedUp", correlationId, causationId, event);
        try {
            authenticationUseCase.signUp(event);
        } catch (Exception e) {
            log.error("User creating failed: {}", e.getMessage());
            String reason = "Notify.Content.CreateFailed|User";
            sendFailureNotification(reason);
        }
        kafkaProducer.send(event, correlationId, causationId);
    }

    private void sendFailureNotification(String reason) {
        commandGateway.sendAndWait(NotificationCommand.SendNotification.builder()
                .id(UUID.randomUUID().toString())
                .title("Notify.Title.CreateFailed|User")
                .content(reason)
                .build());
    }

    @ExceptionHandler(payloadType = UserEvent.SignedUp.class)
    public void handleExceptionForUserCreatedEvent(Exception exception) {
        log.error("Unexpected exception occurred when creating user: {}", exception.getMessage());
    }

}
