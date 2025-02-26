package com.ben.smartcv.user.infrastructure;

import com.ben.smartcv.common.user.UserRegisteredEvent;
import com.ben.smartcv.common.util.Constant;
import com.ben.smartcv.common.contract.event.UserEvent;
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
public class EventPublisher {

    KafkaTemplate<String, UserRegisteredEvent> userRegisteredTemplate;

    public void sendUserRegisteredEvent(UserEvent.UserRegistered event) {
        UserRegisteredEvent protoEvent = UserRegisteredEvent.newBuilder()
                .setUserId(event.getUserId())
                .setEmail(event.getEmail())
                .setFullName(event.getFullName())
                .build();

        userRegisteredTemplate.send(
                Constant.KAFKA_TOPIC_USER_EVENT,
                event.getUserId(),
                protoEvent
        );
    }

}
