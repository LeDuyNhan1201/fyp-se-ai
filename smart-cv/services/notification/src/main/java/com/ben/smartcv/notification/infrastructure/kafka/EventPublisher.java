package com.ben.smartcv.notification.infrastructure.kafka;

import com.ben.smartcv.common.contract.event.NotificationEvent;
import com.ben.smartcv.common.notification.ApprovalMailSentEvent;
import com.ben.smartcv.common.notification.NotificationSentEvent;
import com.ben.smartcv.common.util.Constant;
import com.ben.smartcv.common.util.KafkaHelper;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class EventPublisher {

    KafkaTemplate<String, NotificationSentEvent> notificationSentTemplate;

    KafkaTemplate<String, ApprovalMailSentEvent> approvalMailSentTemplate;

    public void send(NotificationEvent.NotificationSent event, String correlationId, String causationId) {
        NotificationSentEvent protoEvent = NotificationSentEvent.newBuilder()
                .setTitle(event.getTitle())
                .setContent(event.getContent())
                .setLocale(event.getLocale())
                .build();

        ProducerRecord<String, NotificationSentEvent> record = new ProducerRecord<>(
                Constant.KAFKA_TOPIC_NOTIFICATION_EVENT, null, event.getId(), protoEvent,
                KafkaHelper.createHeaders(correlationId, causationId));
        notificationSentTemplate.send(record);
    }

    public void send(NotificationEvent.ApprovalMailSent event, String correlationId, String causationId) {
        ApprovalMailSentEvent protoEvent = ApprovalMailSentEvent.newBuilder()
                .setTitle(event.getTitle())
                .setContent(event.getContent())
                .setLocale(event.getLocale())
                .build();

        ProducerRecord<String, ApprovalMailSentEvent> record = new ProducerRecord<>(
                Constant.KAFKA_TOPIC_NOTIFICATION_EVENT, null, event.getId(), protoEvent,
                KafkaHelper.createHeaders(correlationId, causationId));
        approvalMailSentTemplate.send(record);
    }

}
