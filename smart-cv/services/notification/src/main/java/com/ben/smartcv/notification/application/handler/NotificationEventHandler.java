package com.ben.smartcv.notification.application.handler;

import com.ben.smartcv.common.contract.event.NotificationEvent;
import com.ben.smartcv.common.util.LogHelper;
import com.ben.smartcv.notification.application.usecase.IMailUseCase;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.annotation.MetaDataValue;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class NotificationEventHandler {

    IMailUseCase mailUseCase;

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
        log.info("Approval Mail sent: {}", event.getTitle());
        LogHelper.logMessage(log, "ApprovalMailSent", correlationId, causationId, event);

        mailUseCase.sendApprovalMail(event);
    }

}
