package com.ben.smartcv.curriculum_vitae.infrastructure;

import com.ben.smartcv.common.contract.event.CvEvent;
import com.ben.smartcv.common.cv.CvDeletedEvent;
import com.ben.smartcv.common.cv.CvProcessedEvent;
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
public class EventPublisher {

    KafkaTemplate<String, CvProcessedEvent> cvProcessedEventTemplate;

    KafkaTemplate<String, CvDeletedEvent> cvDeletedEventTemplate;

    public void send(CvEvent.CvProcessed event) {
        CvProcessedEvent protoEvent = CvProcessedEvent.newBuilder()
                .setCvId(event.getCvId())
                .setObjectKey(event.getObjectKey())
                .build();

        cvProcessedEventTemplate.send(
                Constant.KAFKA_TOPIC_CV_EVENT,
                event.getCvId(),
                protoEvent
        );
    }

    public void send(CvEvent.CvDeleted event) {
        CvDeletedEvent protoEvent = CvDeletedEvent.newBuilder()
                .setCvId(event.getCvId())
                .build();

        cvDeletedEventTemplate.send(
                Constant.KAFKA_TOPIC_CV_EVENT,
                event.getCvId(),
                protoEvent
        );
    }
}
