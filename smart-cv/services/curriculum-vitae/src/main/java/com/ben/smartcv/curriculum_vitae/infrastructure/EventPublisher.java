package com.ben.smartcv.curriculum_vitae.infrastructure;

import com.ben.smartcv.common.contract.event.CvEvent;
import com.ben.smartcv.common.cv.CvParsedEvent;
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

    KafkaTemplate<String, CvParsedEvent> cvParsedEventTemplate;

    public void send(CvEvent.CvParsed event) {
        CvParsedEvent protoEvent = CvParsedEvent.newBuilder()
                .setCvId(event.getCvId())
                .build();

        cvParsedEventTemplate.send(
                Constant.KAFKA_TOPIC_CV_EVENT,
                event.getCvId(),
                protoEvent
        );
    }

}
