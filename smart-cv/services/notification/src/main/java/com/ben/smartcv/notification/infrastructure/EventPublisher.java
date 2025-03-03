package com.ben.smartcv.notification.infrastructure;

import com.ben.smartcv.common.contract.event.NotificationEvent;
import com.ben.smartcv.common.notification.NotificationSentEvent;
import com.ben.smartcv.common.util.Constant;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class EventPublisher {

    KafkaTemplate<String, NotificationSentEvent> notificationSentTemplate;

    public void send(NotificationEvent.NotificationSent event) {
        NotificationSentEvent protoEvent = NotificationSentEvent.newBuilder()
                .setTitle(event.getTitle())
                .setContent(event.getContent())
                .build();

        notificationSentTemplate.send(
                Constant.KAFKA_TOPIC_NOTIFICATION_EVENT,
                UUID.randomUUID().toString(),
                protoEvent
        );
    }

}
