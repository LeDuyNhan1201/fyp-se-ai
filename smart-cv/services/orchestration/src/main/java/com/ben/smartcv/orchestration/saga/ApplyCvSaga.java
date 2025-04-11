package com.ben.smartcv.orchestration.saga;

import com.ben.smartcv.common.contract.command.CvCommand;
import com.ben.smartcv.common.contract.command.NotificationCommand;
import com.ben.smartcv.common.contract.event.CvEvent;
import com.ben.smartcv.common.cv.CvAppliedEvent;
import com.ben.smartcv.common.cv.CvProcessedEvent;
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
public class ApplyCvSaga {

    @Autowired
    transient CommandGateway commandGateway;

    static final String ASSOCIATION_PROPERTY = "objectKey";

    @StartSaga
    @SagaEventHandler(associationProperty = ASSOCIATION_PROPERTY)
    public void on(CvEvent.CvApplied event,
                   @MetaDataValue("correlationId") String correlationId,
                   @MetaDataValue("causationId") String causationId) {
        // 5
        LogHelper.logMessage(log, "5|CvApplied", correlationId, causationId, event);
        String identifier = UUID.randomUUID().toString();
        commandGateway.sendAndWait(CvCommand.ProcessCv.builder()
                .id(identifier)
                .objectKey(event.getObjectKey())
                .jobId(event.getJobId())
                .build(), MetaData.with("correlationId", identifier).and("causationId", event.getId()));
    }

    @EndSaga
    @SagaEventHandler(associationProperty = ASSOCIATION_PROPERTY)
    public void on(CvEvent.CvProcessed event,
                   @MetaDataValue("correlationId") String correlationId,
                   @MetaDataValue("causationId") String causationId) {
        // 10
        LogHelper.logMessage(log, "10|CvProcessed", correlationId, causationId, event);
        String identifier = UUID.randomUUID().toString();
        commandGateway.sendAndWait(NotificationCommand.SendNotification.builder()
                .id(identifier)
                .title("Notify.Title.ProcessSuccessfully|CV")
                .content("Notify.Content.ProcessSuccessfully|CV")
                .build(), MetaData.with("correlationId", identifier).and("causationId", event.getId()));
    }

    @KafkaListener(
            id = "saga.cv-applied-event",
            topics = Constant.KAFKA_TOPIC_CV_EVENT,
            groupId = Constant.KAFKA_GROUP_ORCHESTRATION)
    public void consume(CvAppliedEvent event,
                        @Header(name = "correlationId", required = false) String correlationId,
                        @Header(name = "causationId", required = false) String causationId) {
        // 4
        on(CvEvent.CvApplied.builder()
                .objectKey(event.getObjectKey())
                .jobId(event.getJobId())
                .build(), correlationId, causationId);
    }

    @KafkaListener(
            id = "saga.cv-processed-event",
            topics = Constant.KAFKA_TOPIC_CV_EVENT,
            groupId = Constant.KAFKA_GROUP_ORCHESTRATION)
    public void consume(CvProcessedEvent event,
                        @Header(name = "correlationId", required = false) String correlationId,
                        @Header(name = "causationId", required = false) String causationId) {
        // 9
        on(CvEvent.CvProcessed.builder()
                .objectKey(event.getObjectKey())
                .jobId(event.getJobId())
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
