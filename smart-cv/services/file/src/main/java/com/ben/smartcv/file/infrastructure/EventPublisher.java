package com.ben.smartcv.file.infrastructure;

import com.ben.smartcv.common.contract.event.CvEvent;
import com.ben.smartcv.common.cv.CvAppliedEvent;
import com.ben.smartcv.common.cv.CvFileDeletedEvent;
import com.ben.smartcv.common.util.Constant;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class EventPublisher {

    KafkaTemplate<String, CvAppliedEvent> cvAppliedEventTemplate;

    KafkaTemplate<String, CvFileDeletedEvent> cvFileDeletedEventTemplate;

    public void send(CvEvent.CvApplied event) {
        CvAppliedEvent protoEvent = CvAppliedEvent.newBuilder()
                .setFileName(event.getFileName())
                .setCvId(event.getCvId())
                .build();

        ProducerRecord<String, CvAppliedEvent> record = new ProducerRecord<>(
                Constant.KAFKA_TOPIC_CV_EVENT, null, event.getCvId(), protoEvent,
                List.of(new RecordHeader("correlationId", event.getId().getBytes(StandardCharsets.UTF_8)),
                        new RecordHeader("causationId", event.getId().getBytes(StandardCharsets.UTF_8))));
        cvAppliedEventTemplate.send(record);
    }

    public void send(CvEvent.CvFileDeleted event) {
        CvFileDeletedEvent protoEvent = CvFileDeletedEvent.newBuilder()
                .setCvId(event.getCvId())
                .build();

        ProducerRecord<String, CvFileDeletedEvent> record = new ProducerRecord<>(
                Constant.KAFKA_TOPIC_CV_EVENT, null, event.getCvId(), protoEvent,
                List.of(new RecordHeader("correlationId", event.getId().getBytes(StandardCharsets.UTF_8)),
                        new RecordHeader("causationId", event.getId().getBytes(StandardCharsets.UTF_8))));
        cvFileDeletedEventTemplate.send(record);
    }

}
