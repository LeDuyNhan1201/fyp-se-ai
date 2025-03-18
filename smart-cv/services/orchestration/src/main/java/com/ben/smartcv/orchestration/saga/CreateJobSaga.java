package com.ben.smartcv.orchestration.saga;

import com.ben.smartcv.common.contract.command.JobCommand;
import com.ben.smartcv.common.contract.command.NotificationCommand;
import com.ben.smartcv.common.contract.event.JobEvent;
import com.ben.smartcv.common.contract.event.NotificationEvent;
import com.ben.smartcv.common.job.JobCreatedEvent;
import com.ben.smartcv.common.job.JobDeletedEvent;
import com.ben.smartcv.common.job.JobProcessedEvent;
import com.ben.smartcv.common.notification.NotificationSentEvent;
import com.ben.smartcv.common.util.Constant;
import com.ben.smartcv.common.util.EventLogger;
import com.ben.smartcv.common.util.TimeHelper;
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
public class CreateJobSaga {

    @Autowired
    transient CommandGateway commandGateway;

    @Autowired
    transient CommandPublisher commandPublisher;

    static final String ASSOCIATION_PROPERTY = "jobId";

    @StartSaga
    @SagaEventHandler(associationProperty = ASSOCIATION_PROPERTY)
    public void on(JobEvent.JobCreated event) {
        // 5
        log.info(EventLogger.logEvent("JobCreated",
                event.getJobId(), event.getJobId(), Map.of("jobId", event.getJobId())));

        commandGateway.sendAndWait(JobCommand.ProcessJob.builder()
                .id(UUID.randomUUID().toString())
                .jobId(event.getJobId())
                .build());
    }

    @SagaEventHandler(associationProperty = ASSOCIATION_PROPERTY)
    public void on(JobEvent.JobProcessed event) {
//        try {
//            // 10
//            commandPublisher.send(JobCommand.ProcessJob.builder()
//                    .jobId(event.getJobId())
//                    .build());
//
//        } catch (Exception e) {
//            // send command to notification service
//            commandGateway.sendAndWait(JobCommand.RollbackProcessJob.builder()
//                    .id(UUID.randomUUID().toString())
//                    .jobId(event.getJobId())
//                    .build());
//        }
        log.info(EventLogger.logEvent("JobProcessed",
                event.getJobId(), event.getJobId(), Map.of("jobId", event.getJobId())));

        commandGateway.sendAndWait(NotificationCommand.SendNotification.builder()
                .id(UUID.randomUUID().toString())
                .title("Job process successfully")
                .content("Job processing failed successfully jobId: " + event.getJobId())
                .build());
    }

    @EndSaga
    @SagaEventHandler(associationProperty = ASSOCIATION_PROPERTY)
    public void on(JobEvent.JobDeleted event) {
        log.info(EventLogger.logEvent("JobDeleted",
                event.getJobId(), event.getJobId(), Map.of("jobId", event.getJobId())));

        commandGateway.sendAndWait(NotificationCommand.SendNotification.builder()
                .id(UUID.randomUUID().toString())
                .title("Job process Failed")
                .content("Job processing failed for jobId: " + event.getJobId() + "please try again")
                .build());
    }

//    @EndSaga
//    @SagaEventHandler(associationProperty = "associationProperty")
//    public void on(NotificationEvent.NotificationSent event) {
//        log.info(EventLogger.logEvent("NotificationSent",
//                event.getAssociationProperty(), event.getAssociationProperty(), Map.of("title", event.getTitle())));
//
//        associateWith("associationProperty", event.getAssociationProperty());
//        log.info("End create Job saga");
//    }

    @KafkaListener(topics = Constant.KAFKA_TOPIC_JOB_EVENT,
            groupId = Constant.KAFKA_GROUP_ORCHESTRATION)
    public void consume(JobCreatedEvent event) {
        on(JobEvent.JobCreated.builder()
                .jobId(event.getJobId())
                .organizationName(event.getOrganizationName())
                .position(event.getPosition())
                .fromSalary(event.getFromSalary())
                .toSalary(event.getToSalary())
                .expiredAt(TimeHelper.convertToInstant(event.getExpiredAt()))
                .requirements(event.getRequirements())
                .build());
    }

    @KafkaListener(topics = Constant.KAFKA_TOPIC_JOB_EVENT,
            groupId = Constant.KAFKA_GROUP_ORCHESTRATION)
    public void consume(JobProcessedEvent event) {
        on(JobEvent.JobProcessed.builder()
                .jobId(event.getJobId())
                .build());
    }

    @KafkaListener(topics = Constant.KAFKA_TOPIC_JOB_EVENT,
            groupId = Constant.KAFKA_GROUP_ORCHESTRATION)
    public void consume(JobDeletedEvent event) {
        on(JobEvent.JobDeleted.builder()
                .jobId(event.getJobId())
                .build());
    }

//    @KafkaListener(topics = Constant.KAFKA_TOPIC_NOTIFICATION_EVENT,
//            groupId = Constant.KAFKA_GROUP_ORCHESTRATION)
//    public void consume(NotificationSentEvent event) {
//        // 10
//        log.info("lsdfjaslkdfjdklsfjkdlsfjkldfjsklfjksldfjsklfjlksdfdjliksf");
//        if (event.getTitle().toLowerCase().contains("job")) {
//            log.info("Finish creating Job");
//            on(NotificationEvent.NotificationSent.builder()
//                    .associationProperty(event.getAssociationProperty())
//                    .title(event.getTitle())
//                    .content(event.getContent())
//                    .build());
//        }
//    }

    @ExceptionHandler(resultType = Exception.class, payloadType = JobEvent.JobCreated.class)
    public void handleExceptionForJobCreatedEvent(Exception exception) {
        log.error("Unexpected Exception occurred when applied cv: {}", exception.getMessage());
    }

    @ExceptionHandler(resultType = Exception.class, payloadType = JobEvent.JobProcessed.class)
    public void handleExceptionJobProcessedEvent(Exception exception) {
        log.error("Unexpected Exception occurred when processed cv: {}", exception.getMessage());
    }

}
