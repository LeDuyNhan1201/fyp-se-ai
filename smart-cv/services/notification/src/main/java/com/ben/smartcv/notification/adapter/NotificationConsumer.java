package com.ben.smartcv.notification.adapter;

import com.ben.smartcv.common.contract.command.NotificationCommand;
import com.ben.smartcv.common.notification.SendNotificationCommand;
import com.ben.smartcv.common.util.Constant;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class NotificationConsumer {

    CommandGateway commandGateway;

    @KafkaListener(topics = Constant.KAFKA_TOPIC_NOTIFICATION_COMMAND,
            groupId = Constant.KAFKA_GROUP_NOTIFICATION)
    public void consume(SendNotificationCommand command) {
        commandGateway.send(NotificationCommand.SendNotification.builder()
                .id(UUID.randomUUID().toString())
                .title(command.getTitle())
                .content(command.getContent())
                .associationProperty(command.getAssociationProperty())
                .build());
    }

}
