package com.ben.smartcv.common.component;

import com.ben.smartcv.common.contract.command.NotificationCommand;
import com.ben.smartcv.common.notification.SendNotificationCommand;
import com.ben.smartcv.common.util.Constant;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class CommonEventPublisher {

    KafkaTemplate<String, SendNotificationCommand> sendNotificationCommandTemplate;

    public void send(NotificationCommand.SendNotification event) {
        SendNotificationCommand protoEvent = SendNotificationCommand.newBuilder()
                .setAssociationProperty(event.getAssociationProperty())
                .setTitle(event.getTitle())
                .setContent(event.getContent())
                .build();

        sendNotificationCommandTemplate.send(
                Constant.KAFKA_TOPIC_NOTIFICATION_COMMAND,
                event.getAssociationProperty(),
                protoEvent
        );
    }

}
