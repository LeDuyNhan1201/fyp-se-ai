package com.ben.smartcv.user.infrastructure.kafka;

import com.ben.smartcv.common.user.SignedUpEvent;
import com.ben.smartcv.common.util.Constant;
import com.ben.smartcv.common.contract.event.UserEvent;
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

    KafkaTemplate<String, SignedUpEvent> signedUpKafkaTemplate;

    public void send(UserEvent.SignedUp event, String correlationId, String causationId) {

        SignedUpEvent protoEvent = SignedUpEvent.newBuilder()
                .setEmail(event.getEmail())
                .setPassword(event.getPassword())
                .setFirstName(event.getFirstName())
                .setLastName(event.getLastName())
                .build();

        ProducerRecord<String, SignedUpEvent> record = new ProducerRecord<>(
                Constant.KAFKA_TOPIC_USER_EVENT, null, event.getId(), protoEvent,
                KafkaHelper.createHeaders(correlationId, causationId));

        signedUpKafkaTemplate.send(record);
    }

}
