package com.ben.smartcv.job.adapter;

import com.ben.smartcv.common.contract.command.JobCommand;
import com.ben.smartcv.common.job.RollbackProcessJobCommand;
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
public class JobProcessorConsumer {

    CommandGateway commandGateway;

    @KafkaListener(topics = Constant.KAFKA_TOPIC_JOB_COMMAND,
            groupId = Constant.KAFKA_GROUP_JOB_PROCESSOR)
    public void consume(RollbackProcessJobCommand command) {
        commandGateway.send(JobCommand.RollbackProcessJob.builder()
                .id(UUID.randomUUID().toString())
                .jobId(command.getJobId())
                .build());
    }

}
