package com.ben.smartcv.file.infrastructure;

import com.ben.smartcv.common.contract.event.CvEvent;
import com.ben.smartcv.common.cv.CvAppliedEvent;
import com.ben.smartcv.common.cv.CvFileDeletedEvent;
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

    KafkaTemplate<String, CvAppliedEvent> cvAppliedEventTemplate;

    KafkaTemplate<String, CvFileDeletedEvent> cvFileDeletedEventTemplate;

    public void send(CvEvent.CvApplied event) {
        CvAppliedEvent protoEvent = CvAppliedEvent.newBuilder()
                .setFileName(event.getFileName())
                .setCvId(event.getCvId())
                .build();

        cvAppliedEventTemplate.send(
                Constant.KAFKA_TOPIC_CV_EVENT,
                event.getId(),
                protoEvent
        );
    }

    public void send(CvEvent.CvFileDeleted event) {
        CvFileDeletedEvent protoEvent = CvFileDeletedEvent.newBuilder()
                .setCvId(event.getCvId())
                .build();

        cvFileDeletedEventTemplate.send(
                Constant.KAFKA_TOPIC_CV_EVENT,
                event.getId(),
                protoEvent
        );
    }

}
