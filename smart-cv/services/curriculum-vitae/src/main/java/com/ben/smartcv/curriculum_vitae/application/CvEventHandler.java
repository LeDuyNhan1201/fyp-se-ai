package com.ben.smartcv.curriculum_vitae.application;

import com.ben.smartcv.common.contract.event.CvEvent;
import com.ben.smartcv.curriculum_vitae.infrastructure.EventPublisher;
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
    public void on(CvEvent.CvProcessed event) {
        // 8
        kafkaProducer.send(event);
    }

    @EventHandler
    public void on(CvEvent.CvDeleted event) {
        kafkaProducer.send(event);
    }

}
