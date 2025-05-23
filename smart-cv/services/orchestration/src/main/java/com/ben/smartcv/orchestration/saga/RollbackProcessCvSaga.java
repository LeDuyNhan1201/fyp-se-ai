package com.ben.smartcv.orchestration.saga;

import com.ben.smartcv.common.contract.command.CvCommand;
import com.ben.smartcv.common.contract.command.NotificationCommand;
import com.ben.smartcv.common.contract.event.CvEvent;
import com.ben.smartcv.common.cv.CvDeletedEvent;
import com.ben.smartcv.common.cv.CvFileDeletedEvent;
import com.ben.smartcv.common.util.Constant;
import com.ben.smartcv.common.util.LogHelper;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.MetaData;
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
public class RollbackProcessCvSaga {

    @Autowired
    transient CommandGateway commandGateway;

    static final String ASSOCIATION_PROPERTY = "objectKey";

    @StartSaga
    @SagaEventHandler(associationProperty = ASSOCIATION_PROPERTY)
    public void on(CvEvent.CvDeleted event,
                   @MetaDataValue("correlationId") String correlationId,
                   @MetaDataValue("causationId") String causationId) {
        // 6
        LogHelper.logMessage(log, "6|CvDeleted", correlationId, causationId, event);
        String identifier = UUID.randomUUID().toString();
        commandGateway.sendAndWait(CvCommand.DeleteCvFile.builder()
                .id(identifier)
                .objectKey(event.getObjectKey())
                .build(), MetaData.with("correlationId", identifier).and("causationId", event.getId()));
    }

    @EndSaga
    @SagaEventHandler(associationProperty = ASSOCIATION_PROPERTY)
    public void on(CvEvent.CvFileDeleted event,
                   @MetaDataValue("correlationId") String correlationId,
                   @MetaDataValue("causationId") String causationId) {
        // 11
        LogHelper.logMessage(log, "11|CvFileDeleted", correlationId, causationId, event);
        String identifier = UUID.randomUUID().toString();
        commandGateway.sendAndWait(NotificationCommand.SendNotification.builder()
                .id(identifier)
                .title("Notify.Title.ProcessFailed|CV")
                .content("Notify.Content.ProcessFailed|CV")
                .build(), MetaData.with("correlationId", identifier).and("causationId", event.getId()));
    }

    @KafkaListener(
            id = "saga.cv-deleted-event",
            topics = Constant.KAFKA_TOPIC_CV_EVENT,
            groupId = Constant.KAFKA_GROUP_ORCHESTRATION)
    public void consume(CvDeletedEvent event,
                        @Header(name = "correlationId", required = false) String correlationId,
                        @Header(name = "causationId", required = false) String causationId) {
        // 5
        on(CvEvent.CvDeleted.builder()
                .objectKey(event.getObjectKey())
                .build(), correlationId, causationId);
    }

    @KafkaListener(
            id = "saga.cv-file-deleted-event",
            topics = Constant.KAFKA_TOPIC_CV_EVENT,
            groupId = Constant.KAFKA_GROUP_ORCHESTRATION)
    public void consume(CvFileDeletedEvent event,
                        @Header(name = "correlationId", required = false) String correlationId,
                        @Header(name = "causationId", required = false) String causationId) {
        // 10
        on(CvEvent.CvFileDeleted.builder()
                .objectKey(event.getObjectKey())
                .build(), correlationId, causationId);
    }

    @ExceptionHandler(resultType = Exception.class, payloadType = CvEvent.CvDeleted.class)
    public void handleExceptionCvDeletedEvent(Exception exception) {
        log.error("Unexpected Exception occurred when deleted cv: {}", exception.getMessage());
    }

    @ExceptionHandler(resultType = Exception.class, payloadType = CvEvent.CvFileDeleted.class)
    public void handleExceptionCvFileDeletedEvent(Exception exception) {
        log.error("Unexpected Exception occurred when deleted cv file: {}", exception.getMessage());
    }

}
