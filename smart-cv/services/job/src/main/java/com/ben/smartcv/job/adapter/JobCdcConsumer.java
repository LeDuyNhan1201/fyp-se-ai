package com.ben.smartcv.job.adapter;

import com.ben.smartcv.common.contract.command.NotificationCommand;
import com.ben.smartcv.common.infrastructure.kafka.BaseCdcConsumer;
import com.ben.smartcv.common.infrastructure.kafka.RetrySupportDql;
import com.ben.smartcv.common.util.Constant;
import com.ben.smartcv.job.application.usecase.ISlaveJobWriteSideUseCase;
import com.ben.smartcv.job.domain.entity.SlaveJob;
import com.ben.smartcv.job.infrastructure.debezium.CdcOperation;
import com.ben.smartcv.job.infrastructure.debezium.JobCdcMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class JobCdcConsumer extends BaseCdcConsumer<JobCdcMessage.Key, JobCdcMessage> {

    ISlaveJobWriteSideUseCase useCase;

    CommandGateway commandGateway;

    @KafkaListener(
        id = "job-postgres-sync-elastic",
        groupId = Constant.KAFKA_GROUP_JOB_CDC,
        topics = "${debezium.connectors[0].topic}",
        containerFactory = Constant.JOB_CDC_LISTENER_CONTAINER_FACTORY
    )
    @RetrySupportDql(listenerContainerFactory = Constant.JOB_CDC_LISTENER_CONTAINER_FACTORY)
    public void processMessage(
        @Header(KafkaHeaders.RECEIVED_KEY) JobCdcMessage.Key key,
        @Payload(required = false) @Valid JobCdcMessage jobCdcMessage,
        @Headers MessageHeaders headers
    ) {
        log.info("Cdc job {}", jobCdcMessage.getAfter().getOrganizationName());
        processMessage(key, jobCdcMessage, headers, this::sync);

        commandGateway.sendAndWait(NotificationCommand.SendNotification.builder()
                .id(UUID.randomUUID().toString())
                .title("Notify.Title.ItemAvailable")
                .content("Notify.Content.ItemAvailable")
                .build());
    }

    public void sync(JobCdcMessage.Key key, JobCdcMessage jobCdcMessage) {
        boolean isHardDeleteEvent = jobCdcMessage == null
                || CdcOperation.DELETE.equals(jobCdcMessage.getOp());
        if (isHardDeleteEvent) {
            log.warn("Having hard delete event for job: '{}'", key.getId());
            useCase.delete(key.getId());
        } else {
            CdcOperation operation = jobCdcMessage.getOp();
            SlaveJob job = SlaveJob.sync(jobCdcMessage.getAfter());
            switch (operation) {
                case CREATE, READ ->  useCase.create(job);
                case UPDATE -> useCase.update(job);
                default -> log.error("Unsupported operation '{}' for job: '{}'", operation, job);
            }
        }
    }

}