package com.ben.smartcv.orchestration.saga;

import com.ben.smartcv.common.contract.command.CvCommand;
import com.ben.smartcv.common.contract.command.NotificationCommand;
import com.ben.smartcv.common.contract.event.CvEvent;
import com.ben.smartcv.common.contract.event.NotificationEvent;
import com.ben.smartcv.common.cv.CvAppliedEvent;
import com.ben.smartcv.common.cv.CvDeletedEvent;
import com.ben.smartcv.common.cv.CvFileDeletedEvent;
import com.ben.smartcv.common.cv.CvProcessedEvent;
import com.ben.smartcv.common.util.Constant;
import com.ben.smartcv.common.util.EventLogger;
import com.ben.smartcv.orchestration.publisher.CommandPublisher;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.Map;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;
import static org.axonframework.modelling.saga.SagaLifecycle.associateWith;

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
    public void on(CvEvent.CvApplied event) {
        // 5
        log.info(EventLogger.logEvent("CvApplied",
                event.getCvId(), event.getCvId(), Map.of("fileName", event.getFileName())));

        commandGateway.sendAndWait(CvCommand.ProcessCv.builder()
                .id(UUID.randomUUID().toString())
                .cvId(event.getCvId())
                .build());
    }

    @SagaEventHandler(associationProperty = ASSOCIATION_PROPERTY)
    public void on(CvEvent.CvProcessed event) {
        try {
            // 10
            log.info(EventLogger.logEvent("CvProcessed",
                    event.getCvId(), event.getCvId(), Map.of("cvId", event.getCvId())));

            commandPublisher.send(CvCommand.ProcessCv.builder()
                    .cvId(event.getCvId())
                    .objectKey(event.getObjectKey())
                    .build());

        } catch (Exception e) {
            commandGateway.sendAndWait(CvCommand.RollbackProcessCv.builder()
                    .id(UUID.randomUUID().toString())
                    .cvId(event.getCvId())
                    .objectKey(event.getObjectKey())
                    .build());
        }
    }

    @SagaEventHandler(associationProperty = ASSOCIATION_PROPERTY)
    public void on(CvEvent.CvDeleted event) {
        // 6
        log.info(EventLogger.logEvent("CvDeleted",
                event.getCvId(), event.getCvId(), Map.of("cvId", event.getCvId())));
        commandGateway.sendAndWait(CvCommand.DeleteCvFile.builder()
                .id(UUID.randomUUID().toString())
                .cvId(event.getCvId())
                .objectKey(event.getObjectKey())
                .build());
    }

    @SagaEventHandler(associationProperty = ASSOCIATION_PROPERTY)
    public void on(CvEvent.CvFileDeleted event) {
        // 11
        log.info(EventLogger.logEvent("CvFileDeleted",
                event.getCvId(), event.getCvId(), Map.of("objectKey", event.getObjectKey())));
        commandGateway.sendAndWait(NotificationCommand.SendNotification.builder()
                .id(UUID.randomUUID().toString())
                .title("CV Process Failed")
                .content("CV processing failed for cvId: " + event.getCvId() + "please try again")
                .build());
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "associationProperty")
    public void on(NotificationEvent.NotificationSent event) {
        log.info(EventLogger.logEvent("NotificationSent",
                event.getAssociationProperty(), event.getAssociationProperty(), Map.of("title", event.getTitle())));
        associateWith("associationProperty", event.getAssociationProperty());
    }

    @KafkaListener(topics = Constant.KAFKA_TOPIC_CV_EVENT,
            groupId = Constant.KAFKA_GROUP_ORCHESTRATION)
    public void consume(CvAppliedEvent event) {
        // 4
        on(CvEvent.CvApplied.builder()
                .cvId(event.getCvId())
                .fileName(event.getFileName())
                .build());
    }

    @KafkaListener(topics = Constant.KAFKA_TOPIC_CV_EVENT,
            groupId = Constant.KAFKA_GROUP_ORCHESTRATION)
    public void consume(CvProcessedEvent event) {
        // 9
        on(CvEvent.CvProcessed.builder()
                .cvId(event.getCvId())
                .objectKey(event.getObjectKey())
                .build());
    }

    @KafkaListener(topics = Constant.KAFKA_TOPIC_CV_EVENT,
            groupId = Constant.KAFKA_GROUP_ORCHESTRATION)
    public void consume(CvDeletedEvent event) {
        // 5
        on(CvEvent.CvDeleted.builder()
                .cvId(event.getCvId())
                .objectKey(event.getObjectKey())
                .build());
    }

    @KafkaListener(topics = Constant.KAFKA_TOPIC_CV_EVENT,
            groupId = Constant.KAFKA_GROUP_ORCHESTRATION)
    public void consume(CvFileDeletedEvent event) {
        // 10
        on(CvEvent.CvFileDeleted.builder()
                .cvId(event.getCvId())
                .objectKey(event.getObjectKey())
                .build());
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
