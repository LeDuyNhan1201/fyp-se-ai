package com.ben.smartcv.file.infrastructure;

import com.ben.smartcv.common.contract.event.CvEvent;
import com.ben.smartcv.common.cv.CvAppliedEvent;
import com.ben.smartcv.common.cv.CvFileDeletedEvent;
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

    KafkaTemplate<String, CvAppliedEvent> cvAppliedEventTemplate;

    KafkaTemplate<String, CvFileDeletedEvent> cvFileDeletedEventTemplate;

    public void send(CvEvent.CvApplied event, String correlationId, String causationId) {
        CvAppliedEvent protoEvent = CvAppliedEvent.newBuilder()
                .setObjectKey(event.getObjectKey())
                .setJobId(event.getJobId())
                .build();

        ProducerRecord<String, CvAppliedEvent> record = new ProducerRecord<>(
                Constant.KAFKA_TOPIC_CV_EVENT, null, event.getObjectKey(), protoEvent,
                KafkaHelper.createHeaders(correlationId, causationId));
        cvAppliedEventTemplate.send(record);
    }

    public void send(CvEvent.CvFileDeleted event, String correlationId, String causationId) {
        CvFileDeletedEvent protoEvent = CvFileDeletedEvent.newBuilder()
                .setObjectKey(event.getObjectKey())
                .build();

        ProducerRecord<String, CvFileDeletedEvent> record = new ProducerRecord<>(
                Constant.KAFKA_TOPIC_CV_EVENT, null, event.getObjectKey(), protoEvent,
                KafkaHelper.createHeaders(correlationId, causationId));
        cvFileDeletedEventTemplate.send(record);
    }

}
