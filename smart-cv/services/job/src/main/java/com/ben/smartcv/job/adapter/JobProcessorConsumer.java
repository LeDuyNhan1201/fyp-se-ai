package com.ben.smartcv.job.adapter;

import com.ben.smartcv.common.contract.command.JobCommand;
import com.ben.smartcv.common.job.RollbackProcessJobCommand;
import com.ben.smartcv.common.util.Constant;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.util.JsonFormat;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class JobProcessorConsumer {

    CommandGateway commandGateway;

    @KafkaListener(topics = Constant.KAFKA_TOPIC_JOB_COMMAND,
            groupId = Constant.KAFKA_GROUP_JOB_PROCESSOR)
    public void consume(ConsumerRecord<String, DynamicMessage> record) {
        try {
            Header typeHeader = record.headers().lastHeader("type");
            String typeValue = typeHeader != null ? new String(typeHeader.value(), StandardCharsets.UTF_8) : "Unknown";
            log.info("Received header type: {}", typeValue);

            // Convert DynamicMessage → JSON → Java Object
            DynamicMessage dynamicMessage = record.value();
            String jsonString = JsonFormat.printer().print(dynamicMessage);

            if (RollbackProcessJobCommand.class.getName().contains(typeValue)) {
                // Convert JSON → SendNotificationCommand (Protobuf object)
                RollbackProcessJobCommand.Builder builder = RollbackProcessJobCommand.newBuilder();
                JsonFormat.parser().merge(jsonString, builder);
                RollbackProcessJobCommand command = builder.build();
                log.info("Received command: {}", command);

                commandGateway.send(JobCommand.RollbackProcessJob.builder()
                        .id(UUID.randomUUID().toString())
                        .jobId(command.getJobId())
                        .build());
            }

        } catch (Exception e) {
            log.error("Failed to deserialize message", e);
        }
    }

}
