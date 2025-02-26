package com.ben.smartcv.orchestration.saga;

import com.ben.smartcv.common.contract.command.CvCommand;
import com.ben.smartcv.common.contract.event.CvEvent;
import com.ben.smartcv.common.cv.CvAppliedEvent;
import com.ben.smartcv.common.util.Constant;
import com.ben.smartcv.common.util.EventLogger;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.Map;

import static lombok.AccessLevel.PRIVATE;
import static org.axonframework.modelling.saga.SagaLifecycle.end;

@Saga
@Slf4j
@FieldDefaults(level = PRIVATE)
public class ApplyCvSaga {

    @Autowired
    transient CommandGateway commandGateway;

    static final String ASSOCIATION_PROPERTY = "cvId";

    @StartSaga
    @SagaEventHandler(associationProperty = ASSOCIATION_PROPERTY)
    public void on(CvEvent.CvApplied event) {
        log.info(EventLogger.logEvent("CvApplied", event.getCvId(), event.getCvId(),
                Map.of("userId", event.getUserId())));

        commandGateway.send(new CvCommand.ParseCv(event.getCvId()));
    }

    @SagaEventHandler(associationProperty = ASSOCIATION_PROPERTY)
    public void on(CvEvent.CvApplicationFailed event) {
        end();
    }

    @SagaEventHandler(associationProperty = ASSOCIATION_PROPERTY)
    public void on(CvEvent.CvParsed event) {
        end();
    }

    @SagaEventHandler(associationProperty = ASSOCIATION_PROPERTY)
    public void on(CvEvent.CvParsingFailed event) {
        end();
    }

    @KafkaListener(topics = Constant.KAFKA_TOPIC_CV_EVENT,
            groupId = Constant.KAFKA_GROUP_ORCHESTRATION)
    public void consumeCvEvent(CvAppliedEvent event) {
        on(CvEvent.CvApplied.builder()
                .cvId(event.getCvId())
                .userId(event.getUserId())
                .build());
    }

    @KafkaListener(topics = Constant.KAFKA_TOPIC_CV_EVENT,
            groupId = Constant.KAFKA_GROUP_ORCHESTRATION)
    public void consumeCvEvent(CvEvent.CvApplicationFailed event) {
        on(CvEvent.CvApplicationFailed.builder()
                .cvId(event.getCvId())
                .reason(event.getReason())
                .build());
    }

    @KafkaListener(topics = Constant.KAFKA_TOPIC_CV_EVENT,
            groupId = Constant.KAFKA_GROUP_ORCHESTRATION)
    public void consumeCvEvent(CvEvent.CvParsed event) {
        on(CvEvent.CvParsed.builder()
                .cvId(event.getCvId())
                .build());
    }

    @KafkaListener(topics = Constant.KAFKA_TOPIC_CV_EVENT,
            groupId = Constant.KAFKA_GROUP_ORCHESTRATION)
    public void consumeCvEvent(CvEvent.CvParsingFailed event) {
        on(CvEvent.CvParsingFailed.builder()
                .cvId(event.getCvId())
                .reason(event.getReason())
                .build());
    }

}
