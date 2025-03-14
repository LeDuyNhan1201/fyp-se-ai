package com.ben.smartcv.curriculum_vitae.adapter;

import com.ben.smartcv.common.contract.command.CvCommand;
import com.ben.smartcv.common.cv.RollbackProcessCvCommand;
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
public class CvProcessorConsumer {

    CommandGateway commandGateway;

    @KafkaListener(topics = Constant.KAFKA_TOPIC_CV_COMMAND,
            groupId = Constant.KAFKA_GROUP_CV_PROCESSOR)
    public void consume(ConsumerRecord<String, DynamicMessage> record) {
        // 1
        try {
            Header typeHeader = record.headers().lastHeader("type");
            String typeValue = typeHeader != null ? new String(typeHeader.value(), StandardCharsets.UTF_8) : "Unknown";
            log.info("Received header type: {}", typeValue);

            // Convert DynamicMessage → JSON → Java Object
            DynamicMessage dynamicMessage = record.value();
            String jsonString = JsonFormat.printer().print(dynamicMessage);

            if (RollbackProcessCvCommand.class.getName().contains(typeValue)) {
                // Convert JSON → SendNotificationCommand (Protobuf object)
                RollbackProcessCvCommand.Builder builder = RollbackProcessCvCommand.newBuilder();
                JsonFormat.parser().merge(jsonString, builder);
                RollbackProcessCvCommand command = builder.build();
                log.info("Received command: {}", command);

                commandGateway.send(CvCommand.RollbackProcessCv.builder()
                        .id(UUID.randomUUID().toString())
                        .cvId(command.getCvId())
                        .objectKey(command.getObjectKey())
                        .build());
            }

        } catch (Exception e) {
            log.error("Failed to deserialize message", e);
        }
    }

}
