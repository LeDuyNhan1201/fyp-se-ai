package com.ben.smartcv.file.application.usecase;

import com.ben.smartcv.common.contract.event.CvEvent;
import com.ben.smartcv.file.infrastructure.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class CvEventHandler {

    EventPublisher kafkaProducer;

    @EventHandler
    public void on(CvEvent.CvApplied event) {
        kafkaProducer.sendCvAppliedEvent(event);
    }

    @EventHandler
    public void on(CvEvent.CvApplicationFailed event) {
        kafkaProducer.sendCvApplicationFailedEvent(event);
    }

    @EventHandler
    public void on(CvEvent.CvParsed event) {
        kafkaProducer.sendCCvParsedEvent(event);
    }

    @EventHandler
    public void on(CvEvent.CvParsingFailed event) {
        kafkaProducer.sendCCvParsingFailedEvent(event);
    }

}
