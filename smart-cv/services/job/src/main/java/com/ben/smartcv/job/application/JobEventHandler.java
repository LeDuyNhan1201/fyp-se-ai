package com.ben.smartcv.job.application;

import com.ben.smartcv.common.contract.event.JobEvent;
import com.ben.smartcv.job.infrastructure.EventPublisher;
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
public class JobEventHandler {

    EventPublisher kafkaProducer;

    @EventHandler
    public void on(JobEvent.JobProcessed event) {
        kafkaProducer.send(event);
    }

    @EventHandler
    public void on(JobEvent.JobDeleted event) {
        kafkaProducer.send(event);
    }

}
