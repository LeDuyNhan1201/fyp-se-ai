package com.ben.smartcv.curriculum_vitae.infrastructure;

import com.ben.smartcv.common.contract.event.CvEvent;
import com.ben.smartcv.common.cv.CvDeletedEvent;
import com.ben.smartcv.common.cv.CvProcessedEvent;
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

    KafkaTemplate<String, CvProcessedEvent> cvProcessedEventTemplate;

    KafkaTemplate<String, CvDeletedEvent> cvDeletedEventTemplate;

    public void send(CvEvent.CvProcessed event, String correlationId, String causationId) {
        CvProcessedEvent protoEvent = CvProcessedEvent.newBuilder()
                .setObjectKey(event.getObjectKey())
                .setJobId(event.getJobId())
                .setCreatedBy(event.getCreatedBy())
                .build();

        ProducerRecord<String, CvProcessedEvent> record = new ProducerRecord<>(
                Constant.KAFKA_TOPIC_CV_EVENT, null, event.getObjectKey(), protoEvent,
                KafkaHelper.createHeaders(correlationId, causationId));
        cvProcessedEventTemplate.send(record);
    }

    public void send(CvEvent.CvDeleted event, String correlationId, String causationId) {
        CvDeletedEvent protoEvent = CvDeletedEvent.newBuilder()
                .setObjectKey(event.getObjectKey())
                .build();

        ProducerRecord<String, CvDeletedEvent> record = new ProducerRecord<>(
                Constant.KAFKA_TOPIC_CV_EVENT, null, event.getObjectKey(), protoEvent,
                KafkaHelper.createHeaders(correlationId, causationId));
        cvDeletedEventTemplate.send(record);
    }
}
