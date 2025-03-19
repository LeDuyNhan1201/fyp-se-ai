package com.ben.smartcv.orchestration.saga;

import com.ben.smartcv.common.contract.command.CvCommand;
import com.ben.smartcv.common.contract.command.NotificationCommand;
import com.ben.smartcv.common.contract.event.CvEvent;
import com.ben.smartcv.common.cv.CvAppliedEvent;
import com.ben.smartcv.common.cv.CvDeletedEvent;
import com.ben.smartcv.common.cv.CvFileDeletedEvent;
import com.ben.smartcv.common.cv.CvProcessedEvent;
import com.ben.smartcv.common.util.Constant;
import com.ben.smartcv.common.util.LogHelper;
import com.ben.smartcv.orchestration.publisher.CommandPublisher;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.annotation.MetaDataValue;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;

import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Saga
@Slf4j
@FieldDefaults(level = PRIVATE)
public class ApplyCvSaga {

    @Autowired
    transient CommandGateway commandGateway;

    @Autowired
    transient CommandPublisher commandPublisher;

    static final String ASSOCIATION_PROPERTY = "cvId";

    @StartSaga
    @SagaEventHandler(associationProperty = ASSOCIATION_PROPERTY)
    public void on(CvEvent.CvApplied event,
                   @MetaDataValue("correlationId") String correlationId,
                   @MetaDataValue("causationId") String causationId) {
        // 5
        LogHelper.logMessage(log, "CvApplied", correlationId, causationId, event);
        commandGateway.sendAndWait(CvCommand.ProcessCv.builder()
                .id(UUID.randomUUID().toString())
                .cvId(event.getCvId())
                .build());
    }

    @EndSaga
    @SagaEventHandler(associationProperty = ASSOCIATION_PROPERTY)
    public void on(CvEvent.CvProcessed event,
                   @MetaDataValue("correlationId") String correlationId,
                   @MetaDataValue("causationId") String causationId) {
        // 10
        LogHelper.logMessage(log, "CvProcessed", correlationId, causationId, event);
        commandGateway.sendAndWait(NotificationCommand.SendNotification.builder()
                .id(UUID.randomUUID().toString())
                .title("CV process successfully")
                .content("CV processing successfully for cvId: " + event.getCvId())
                .build());
    }

    @SagaEventHandler(associationProperty = ASSOCIATION_PROPERTY)
    public void on(CvEvent.CvDeleted event,
                   @MetaDataValue("correlationId") String correlationId,
                   @MetaDataValue("causationId") String causationId) {
        // 6
        LogHelper.logMessage(log, "CvDeleted", correlationId, causationId, event);
        commandGateway.sendAndWait(CvCommand.DeleteCvFile.builder()
                .id(UUID.randomUUID().toString())
                .cvId(event.getCvId())
                .objectKey(event.getObjectKey())
                .build());
    }

    @EndSaga
    @SagaEventHandler(associationProperty = ASSOCIATION_PROPERTY)
    public void on(CvEvent.CvFileDeleted event,
                   @MetaDataValue("correlationId") String correlationId,
                   @MetaDataValue("causationId") String causationId) {
        // 11
        LogHelper.logMessage(log, "CvFileDeleted", correlationId, causationId, event);
        commandGateway.sendAndWait(NotificationCommand.SendNotification.builder()
                .id(UUID.randomUUID().toString())
                .title("CV Process Failed")
                .content("CV processing failed for cvId: " + event.getCvId() + "please try again")
                .build());
    }

    @KafkaListener(topics = Constant.KAFKA_TOPIC_CV_EVENT,
            groupId = Constant.KAFKA_GROUP_ORCHESTRATION)
    public void consume(CvAppliedEvent event,
                        @Header(name = "correlationId", required = false) String correlationId,
                        @Header(name = "causationId", required = false) String causationId) {
        // 4
        on(CvEvent.CvApplied.builder()
                .cvId(event.getCvId())
                .fileName(event.getFileName())
                .build(), correlationId, causationId);
    }

    @KafkaListener(topics = Constant.KAFKA_TOPIC_CV_EVENT,
            groupId = Constant.KAFKA_GROUP_ORCHESTRATION)
    public void consume(CvProcessedEvent event,
                        @Header(name = "correlationId", required = false) String correlationId,
                        @Header(name = "causationId", required = false) String causationId) {
        // 9
        on(CvEvent.CvProcessed.builder()
                .cvId(event.getCvId())
                .objectKey(event.getObjectKey())
                .build(), correlationId, causationId);
    }

    @KafkaListener(topics = Constant.KAFKA_TOPIC_CV_EVENT,
            groupId = Constant.KAFKA_GROUP_ORCHESTRATION)
    public void consume(CvDeletedEvent event,
                        @Header(name = "correlationId", required = false) String correlationId,
                        @Header(name = "causationId", required = false) String causationId) {
        // 5
        on(CvEvent.CvDeleted.builder()
                .cvId(event.getCvId())
                .objectKey(event.getObjectKey())
                .build(), correlationId, causationId);
    }

    @KafkaListener(topics = Constant.KAFKA_TOPIC_CV_EVENT,
            groupId = Constant.KAFKA_GROUP_ORCHESTRATION)
    public void consume(CvFileDeletedEvent event,
                        @Header(name = "correlationId", required = false) String correlationId,
                        @Header(name = "causationId", required = false) String causationId) {
        // 10
        on(CvEvent.CvFileDeleted.builder()
                .cvId(event.getCvId())
                .objectKey(event.getObjectKey())
                .build(), correlationId, causationId);
    }

    @ExceptionHandler(resultType = Exception.class, payloadType = CvEvent.CvApplied.class)
    public void handleExceptionForCvAppliedEvent(Exception exception) {
        log.error("Unexpected Exception occurred when applied cv: {}", exception.getMessage());
    }

    @ExceptionHandler(resultType = Exception.class, payloadType = CvEvent.CvProcessed.class)
    public void handleExceptionCvProcessedEvent(Exception exception) {
        log.error("Unexpected Exception occurred when processed cv: {}", exception.getMessage());
    }

}
