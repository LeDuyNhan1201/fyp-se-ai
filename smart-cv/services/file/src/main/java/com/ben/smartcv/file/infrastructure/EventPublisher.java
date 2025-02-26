package com.ben.smartcv.file.infrastructure;

import com.ben.smartcv.common.contract.event.CvEvent;
import com.ben.smartcv.common.cv.CvApplicationFailedEvent;
import com.ben.smartcv.common.cv.CvAppliedEvent;
import com.ben.smartcv.common.cv.CvParsedEvent;
import com.ben.smartcv.common.cv.CvParsingFailedEvent;
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

    KafkaTemplate<String, CvApplicationFailedEvent> cvApplicationFailedEventTemplate;

    KafkaTemplate<String, CvParsedEvent> cvParsedEventTemplate;

    KafkaTemplate<String, CvParsingFailedEvent> cvParsingFailedEventTemplate;

    public void sendCvAppliedEvent(CvEvent.CvApplied event) {
        CvAppliedEvent protoEvent = CvAppliedEvent.newBuilder()
                .setUserId(event.getUserId())
                .setCvId(event.getCvId())
                .build();

        cvAppliedEventTemplate.send(
                Constant.KAFKA_TOPIC_CV_EVENT,
                event.getCvId(),
                protoEvent
        );
    }

    public void sendCvApplicationFailedEvent(CvEvent.CvApplicationFailed event) {
        CvApplicationFailedEvent protoEvent = CvApplicationFailedEvent.newBuilder()
                .setCvId(event.getCvId())
                .setReason(event.getReason())
                .build();

        cvApplicationFailedEventTemplate.send(
                Constant.KAFKA_TOPIC_CV_EVENT,
                event.getCvId(),
                protoEvent
        );
    }

    public void sendCCvParsedEvent(CvEvent.CvParsed event) {
        CvParsedEvent protoEvent = CvParsedEvent.newBuilder()
                .setCvId(event.getCvId())
                .build();

        cvParsedEventTemplate.send(
                Constant.KAFKA_TOPIC_CV_EVENT,
                event.getCvId(),
                protoEvent
        );
    }

    public void sendCCvParsingFailedEvent(CvEvent.CvParsingFailed event) {
        CvParsingFailedEvent protoEvent = CvParsingFailedEvent.newBuilder()
                .setCvId(event.getCvId())
                .setReason(event.getReason())
                .build();

        cvParsingFailedEventTemplate.send(
                Constant.KAFKA_TOPIC_CV_EVENT,
                event.getCvId(),
                protoEvent
        );
    }

}
