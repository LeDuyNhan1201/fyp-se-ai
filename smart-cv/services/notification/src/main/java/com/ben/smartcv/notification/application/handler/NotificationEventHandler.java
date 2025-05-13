package com.ben.smartcv.notification.application.handler;

import com.ben.smartcv.common.contract.command.CvCommand;
import com.ben.smartcv.common.contract.command.NotificationCommand;
import com.ben.smartcv.common.contract.event.NotificationEvent;
import com.ben.smartcv.common.util.LogHelper;
import com.ben.smartcv.notification.application.usecase.IMailUseCase;
import com.ben.smartcv.notification.infrastructure.kafka.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.MetaData;
import org.axonframework.messaging.annotation.MetaDataValue;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class NotificationEventHandler {

    IMailUseCase mailUseCase;

    EventPublisher eventPublisher;

    CommandGateway commandGateway;

    @EventHandler
    public void on(NotificationEvent.NotificationSent event,
                   @MetaDataValue("correlationId") String correlationId,
                   @MetaDataValue("causationId") String causationId) {
        log.info("Notification sent: {}", event.getTitle());
        LogHelper.logMessage(log, "NotificationSent", correlationId, causationId, event);

        if (event.getLocale() != null && !event.getLocale().isEmpty()) {
            LocaleContextHolder.setLocale(Locale.of(event.getLocale()));
            log.info("Notification sent to locale: {}", LocaleContextHolder.getLocale().getLanguage());
        }
    }

    @EventHandler
    public void on(NotificationEvent.ApprovalMailSent event,
                   @MetaDataValue("correlationId") String correlationId,
                   @MetaDataValue("causationId") String causationId) {
        log.info("Approval Mail sent: {}", event.getCvId());
        LogHelper.logMessage(log, "ApprovalMailSent", correlationId, causationId, event);

        try {
            mailUseCase.sendApprovalMail(event);
        } catch (Exception e) {
            log.error("Error sending approval mail: {}", e.getMessage());
            rollback(event);
            sendFailureNotification(event.getId());
        }
        eventPublisher.send(event, correlationId, causationId);
    }

    private void rollback(NotificationEvent.ApprovalMailSent event) {
        String identifier = UUID.randomUUID().toString();
        commandGateway.send(CvCommand.RollbackApproveCv.builder()
                .id(identifier)
                .cvId(event.getCvId())
                .build(), MetaData.with("correlationId", identifier).and("causationId", event.getId()));
    }

    private void sendFailureNotification(String causationId) {
        String identifier = UUID.randomUUID().toString();
        commandGateway.sendAndWait(NotificationCommand.SendNotification.builder()
                .id(identifier)
                .title("Notify.Title.SendMailFailed")
                .content("Notify.Content.SendMailFailed")
                .build(), MetaData.with("correlationId", identifier).and("causationId", causationId));
    }

}
