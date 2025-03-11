package com.ben.smartcv.file.application.usecase;

import com.ben.smartcv.common.contract.command.CvCommand;
import com.ben.smartcv.common.contract.event.CvEvent;
import com.ben.smartcv.common.util.Constant;
import com.ben.smartcv.file.application.exception.FileException;
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

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class FileEventHandler {

    EventPublisher kafkaProducer;

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

    @ExceptionHandler(resultType = FileException.class, payloadType = CvEvent.CvFileDeleted.class)
    public void handleExceptionForCvFileDeletedEvent(FileException exception) {
        log.error("File exception occurred: {}", exception.getMessage());
    }

}
