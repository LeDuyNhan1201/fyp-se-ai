package com.ben.smartcv.curriculum_vitae.adapter;

import com.ben.smartcv.common.contract.command.CvCommand;
import com.ben.smartcv.common.cv.RollbackProcessCvCommand;
import com.ben.smartcv.common.util.Constant;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class CvProcessorConsumer {

    CommandGateway commandGateway;

    @KafkaListener(topics = Constant.KAFKA_TOPIC_CV_EVENT,
            groupId = Constant.KAFKA_GROUP_ORCHESTRATION)
    public void consume(RollbackProcessCvCommand command) {
        // 1
        commandGateway.send(CvCommand.RollbackProcessCv.builder()
                .id(UUID.randomUUID().toString())
                .cvId(command.getCvId())
                .objectKey(command.getObjectKey())
                .build());
    }

}
