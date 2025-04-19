package com.ben.smartcv.file.application.handler;

import com.ben.smartcv.common.contract.command.NotificationCommand;
import com.ben.smartcv.common.contract.event.CvEvent;
import com.ben.smartcv.common.util.LogHelper;
import com.ben.smartcv.file.infrastructure.kafka.EventPublisher;
import com.ben.smartcv.file.infrastructure.minio.IMinioClient;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.annotation.MetaDataValue;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class FileEventHandler {

    EventPublisher eventPublisher;

    IMinioClient minioClient;

    CommandGateway commandGateway;

    @Value("${minio.bucket-name}")
    @NonFinal
    String bucketName;

    @EventHandler
    public void on(CvEvent.CvApplied event,
                   @MetaDataValue("correlationId") String correlationId,
                   @MetaDataValue("causationId") String causationId) {
        // 3
        LogHelper.logMessage(log, "3|CvApplied", correlationId, causationId, event);
        eventPublisher.send(event, correlationId, event.getId());
    }

    @EventHandler
    public void on(CvEvent.CvFileDeleted event,
                   @MetaDataValue("correlationId") String correlationId,
                   @MetaDataValue("causationId") String causationId) {
        // 9
        LogHelper.logMessage(log, "9|CvFileDeleted", correlationId, causationId, event);
        try {
            minioClient.deleteObject(event.getObjectKey(), bucketName);
        } catch (Exception e) {
            log.error("Error when deleting file: ", e);
            String reason = "Notify.Content.DeleteFailed|File";
            sendFailureNotification(reason);
        }
        eventPublisher.send(event, correlationId, event.getId());
    }

    @ExceptionHandler(payloadType = CvEvent.CvFileDeleted.class)
    public void handleExceptionForCvFileDeletedEvent(CvEvent.CvFileDeleted event, Exception exception) {
        log.error("Unexpected exception occurred when deleting cv file {}: {}",
                event.getObjectKey(), exception.getMessage());
    }

    private void sendFailureNotification(String reason) {
        commandGateway.sendAndWait(NotificationCommand.SendNotification.builder()
                .id(UUID.randomUUID().toString())
                .title("Notify.Title.DeleteFailed|File")
                .content(reason)
                .build());
    }

}
