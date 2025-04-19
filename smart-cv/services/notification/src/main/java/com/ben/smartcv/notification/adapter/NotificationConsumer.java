package com.ben.smartcv.notification.adapter;

import com.ben.smartcv.common.contract.command.NotificationCommand;
import com.ben.smartcv.common.contract.event.NotificationEvent;
import com.ben.smartcv.common.notification.NotificationSentEvent;
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

//@Component
//@Slf4j
//@RequiredArgsConstructor
//@FieldDefaults(level = PRIVATE, makeFinal = true)
public class NotificationConsumer {

//    CommandGateway commandGateway;
//
//    @KafkaListener(topics = Constant.KAFKA_TOPIC_NOTIFICATION_COMMAND,
//               groupId = Constant.KAFKA_GROUP_NOTIFICATION)
//    public void consume(ConsumerRecord<String, DynamicMessage> record) {
//        try {
//            Header typeHeader = record.headers().lastHeader("type");
//            String typeValue = typeHeader != null ? new String(typeHeader.value(), StandardCharsets.UTF_8) : "Unknown";
//            log.info("Received header type: {}", typeValue);
//
//            // Convert DynamicMessage → JSON → Java Object
//            DynamicMessage dynamicMessage = record.value();
//            String jsonString = JsonFormat.printer().print(dynamicMessage);
//
//            if (SendNotificationCommand.class.getName().contains(typeValue)) {
//                // Convert JSON → SendNotificationCommand (Protobuf object)
//                SendNotificationCommand.Builder builder = SendNotificationCommand.newBuilder();
//                JsonFormat.parser().merge(jsonString, builder);
//                SendNotificationCommand command = builder.build();
//                log.info("Received command: {}", command);
//
//                commandGateway.send(NotificationCommand.SendNotification.builder()
//                        .id(UUID.randomUUID().toString())
//                        .title(command.getTitle())
//                        .content(command.getContent())
//                        .associationProperty(command.getAssociationProperty())
//                        .build());
//            }
//
//        } catch (Exception e) {
//            log.error("Failed to deserialize message", e);
//        }
//    }

//    @KafkaListener(topics = Constant.KAFKA_TOPIC_NOTIFICATION_EVENT,
//            groupId = Constant.KAFKA_GROUP_ORCHESTRATION)
//    public void consume(NotificationSentEvent event) {
//        // 10
//        log.info("lsdfjaslkdfjdklsfjkdlsfjkldfjsklfjksldfjsklfjlksdfdjliksf");
//        if (event.getTitle().toLowerCase().contains("job")) {
//            log.info("Finish creating Job");
//        }
//    }

}
