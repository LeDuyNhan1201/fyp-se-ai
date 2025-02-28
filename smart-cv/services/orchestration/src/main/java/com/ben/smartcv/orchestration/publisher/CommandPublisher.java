package com.ben.smartcv.orchestration.publisher;

import com.ben.smartcv.common.contract.command.CvCommand;
import com.ben.smartcv.common.cv.ParseCvCommand;
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

    KafkaTemplate<String, ParseCvCommand> parseCvCommandTemplate;

    public void send(CvCommand.ParseCv command) {
        ParseCvCommand protoCommand = ParseCvCommand.newBuilder()
                .setCvId(command.getCvId())
                .build();

        parseCvCommandTemplate.send(
                Constant.KAFKA_TOPIC_CV_COMMAND,
                command.getCvId(),
                protoCommand
        );
    }

}
