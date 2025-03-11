package com.ben.smartcv.file.application.handler;

import com.ben.smartcv.common.component.CommonEventPublisher;
import com.ben.smartcv.common.contract.command.NotificationCommand;
import com.ben.smartcv.common.contract.event.CvEvent;
import com.ben.smartcv.file.infrastructure.EventPublisher;
import com.ben.smartcv.file.infrastructure.IMinioClient;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
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

    EventPublisher kafkaProducer;

    CommonEventPublisher commonEventPublisher;

    IMinioClient minioClient;

    @Value("${minio.bucket-name}")
    @NonFinal
    String bucketName;

    @EventHandler
    public void on(CvEvent.CvApplied event) {
        // 3
        kafkaProducer.send(event);
    }

    @EventHandler
    public void on(CvEvent.CvFileDeleted event) {
        // 9
        minioClient.deleteObject(event.getObjectKey(), bucketName);
        kafkaProducer.send(event);
    }

    @ExceptionHandler(payloadType = CvEvent.CvFileDeleted.class)
    public void handleExceptionForCvFileDeletedEvent(Exception exception) {
        log.error("Unexpected exception occurred when creating job: {}", exception.getMessage());
        commonEventPublisher.send(NotificationCommand.SendNotification.builder()
                .id(UUID.randomUUID().toString())
                .title("Delete job Failed")
                .content("Cv is deleted failed, please try again")
                .build());
    }

}
