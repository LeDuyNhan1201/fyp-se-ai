package com.ben.smartcv.orchestration.publisher;

import com.ben.smartcv.common.contract.command.CvCommand;
import com.ben.smartcv.common.cv.ProcessCvCommand;
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

    KafkaTemplate<String, ProcessCvCommand> processCvCommandTemplate;

    public void send(CvCommand.ProcessCv command) {
        ProcessCvCommand protoCommand = ProcessCvCommand.newBuilder()
                .setCvId(command.getCvId())
                .build();

        processCvCommandTemplate.send(
                Constant.KAFKA_TOPIC_CV_COMMAND,
                command.getCvId(),
                protoCommand
        );
    }

}
