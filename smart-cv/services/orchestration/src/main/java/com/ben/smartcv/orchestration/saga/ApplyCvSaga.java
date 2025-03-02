package com.ben.smartcv.orchestration.saga;

import com.ben.smartcv.common.contract.command.CvCommand;
import com.ben.smartcv.common.contract.event.CvEvent;
import com.ben.smartcv.common.cv.CvAppliedEvent;
import com.ben.smartcv.common.cv.CvParsedEvent;
import com.ben.smartcv.common.util.Constant;
import com.ben.smartcv.common.util.EventLogger;
import com.ben.smartcv.orchestration.publisher.CommandPublisher;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.Map;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;
import static org.axonframework.modelling.saga.SagaLifecycle.end;

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
                event.getCvId(), event.getCvId(), Map.of("userId", event.getUserId())));

        commandGateway.sendAndWait(CvCommand.ParseCv.builder()
                .id(UUID.randomUUID().toString())
                .cvId(event.getCvId())
                .build());
    }

    @SagaEventHandler(associationProperty = ASSOCIATION_PROPERTY)
    public void on(CvEvent.CvParsed event) {
        try {
            // 10
            log.info(EventLogger.logCommand("ParseCv",
                    event.getCvId(), null));

            commandPublisher.send(CvCommand.ParseCv.builder()
                    .cvId(event.getCvId())
                    .build());

        } catch (Exception e) {
            // send command to notification service

        } finally {
            end();
        }
    }

    @KafkaListener(topics = Constant.KAFKA_TOPIC_CV_EVENT,
            groupId = Constant.KAFKA_GROUP_ORCHESTRATION)
    public void consume(CvAppliedEvent event) {
        // 4
        on(CvEvent.CvApplied.builder()
                .cvId(event.getCvId())
                .userId(event.getUserId())
                .build());
    }

    @KafkaListener(topics = Constant.KAFKA_TOPIC_CV_EVENT,
            groupId = Constant.KAFKA_GROUP_ORCHESTRATION)
    public void consume(CvParsedEvent event) {
        // 9
        on(CvEvent.CvParsed.builder()
                .cvId(event.getCvId())
                .build());
    }

    @ExceptionHandler(resultType = Exception.class, payloadType = CvEvent.CvParsed.class)
    public void handleException(Exception exception) {
        log.error("Exception occurred: {}", exception.getMessage());
    }

}
