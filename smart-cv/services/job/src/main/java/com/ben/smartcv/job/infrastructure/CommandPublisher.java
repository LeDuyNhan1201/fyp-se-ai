package com.ben.smartcv.job.infrastructure;

import com.ben.smartcv.common.contract.command.JobCommand;
import com.ben.smartcv.common.job.RollbackProcessJobCommand;
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
public class CommandPublisher {

    KafkaTemplate<String, RollbackProcessJobCommand> rollbackProcessJobCommandTemplate;

    public void send(JobCommand.RollbackProcessJob command) {
        RollbackProcessJobCommand protoCommand = RollbackProcessJobCommand.newBuilder()
                .setJobId(command.getJobId())
                .build();

        rollbackProcessJobCommandTemplate.send(
                Constant.KAFKA_TOPIC_JOB_COMMAND,
                command.getJobId(),
                protoCommand
        );
    }

}
