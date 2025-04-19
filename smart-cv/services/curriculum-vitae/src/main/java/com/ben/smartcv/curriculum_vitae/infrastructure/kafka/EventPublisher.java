package com.ben.smartcv.curriculum_vitae.infrastructure.kafka;

import com.ben.smartcv.common.contract.event.CvEvent;
import com.ben.smartcv.common.cv.CvApprovedEvent;
import com.ben.smartcv.common.cv.CvDeletedEvent;
import com.ben.smartcv.common.cv.CvProcessedEvent;
import com.ben.smartcv.common.cv.CvRenewedEvent;
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

    KafkaTemplate<String, CvApprovedEvent> cvApprovedEventTemplate;

    KafkaTemplate<String, CvRenewedEvent> cvRenewedEventTemplate;

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

    public void send(CvEvent.CvApproved event, String correlationId, String causationId) {
        CvApprovedEvent protoEvent = CvApprovedEvent.newBuilder()
                .setCvId(event.getCvId())
                .setTitle(event.getTitle())
                .setContent(event.getContent())
                .setJobId(event.getJobId())
                .setReceiverId(event.getReceiverId())
                .build();

        ProducerRecord<String, CvApprovedEvent> record = new ProducerRecord<>(
                Constant.KAFKA_TOPIC_CV_EVENT, null, event.getCvId(), protoEvent,
                KafkaHelper.createHeaders(correlationId, causationId));
        cvApprovedEventTemplate.send(record);
    }

    public void send(CvEvent.CvRenewed event, String correlationId, String causationId) {
        CvRenewedEvent protoEvent = CvRenewedEvent.newBuilder()
                .setCvId(event.getCvId())
                .build();

        ProducerRecord<String, CvRenewedEvent> record = new ProducerRecord<>(
                Constant.KAFKA_TOPIC_CV_EVENT, null, event.getCvId(), protoEvent,
                KafkaHelper.createHeaders(correlationId, causationId));
        cvRenewedEventTemplate.send(record);
    }

}
