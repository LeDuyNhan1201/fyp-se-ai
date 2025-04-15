package com.ben.smartcv.curriculum_vitae.application.handler;

import com.ben.smartcv.common.contract.command.CvCommand;
import com.ben.smartcv.common.contract.event.CvEvent;
import com.ben.smartcv.common.cv.ExtractedCvData;
import com.ben.smartcv.common.util.LogHelper;
import com.ben.smartcv.curriculum_vitae.application.usecase.ICvCommandUseCase;
import com.ben.smartcv.curriculum_vitae.domain.model.CurriculumVitae;
import com.ben.smartcv.curriculum_vitae.infrastructure.kafka.EventPublisher;
import com.ben.smartcv.curriculum_vitae.infrastructure.grpc.GrpcClientCvProcessor;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.MetaData;
import org.axonframework.messaging.annotation.MetaDataValue;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class CvEventHandler {

    EventPublisher eventPublisher;

    CommandGateway commandGateway;

    ICvCommandUseCase commandUseCase;

    GrpcClientCvProcessor grpcClientCvProcessor;

    @EventHandler
    public void on(CvEvent.CvProcessed event,
                   @MetaDataValue("correlationId") String correlationId,
                   @MetaDataValue("causationId") String causationId) {
        // 8
        LogHelper.logMessage(log, "8|CvProcessed", correlationId, causationId, event);
        try {
            ExtractedCvData extractedCvData = grpcClientCvProcessor.callExtractData(event);

            CurriculumVitae curriculumVitae = CurriculumVitae.builder()
                    .objectKey(event.getObjectKey())
                    .jobId(event.getJobId())
                    .createdBy(event.getCreatedBy())

                    .name(extractedCvData.getName())
                    .email(extractedCvData.getEmail())
                    .phone(extractedCvData.getPhone())
                    .educations(extractedCvData.getEducationsList())
                    .skills(extractedCvData.getSkillsList())
                    .experiences(extractedCvData.getExperiencesList())
                    .score(extractedCvData.getScore())
                    .build();
            commandUseCase.create(curriculumVitae);

        } catch (Exception e) {
            log.error("Cv processing failed: {}", e.getMessage());
            if (e instanceof StatusRuntimeException) {
                Status status = ((StatusRuntimeException) e).getStatus();
                if (status.getCode() == Status.Code.INVALID_ARGUMENT) {
                    log.error("Invalid argument: {}", status.getDescription());
                } else {
                    log.error("Unexpected error: {}", status.getDescription());
                }
            }
            rollback(event);
        }
        eventPublisher.send(event, correlationId, causationId);
    }

    @EventHandler
    public void on(CvEvent.CvDeleted event,
                   @MetaDataValue("correlationId") String correlationId,
                   @MetaDataValue("causationId") String causationId) {
        // 4
        LogHelper.logMessage(log, "4|CvDeleted", correlationId, causationId, event);
        eventPublisher.send(event, correlationId, causationId);
    }

    private void rollback(CvEvent.CvProcessed event) {
        String identifier = UUID.randomUUID().toString();
        commandGateway.send(CvCommand.RollbackProcessCv.builder()
                .id(identifier)
                .objectKey(event.getObjectKey())
                .build(), MetaData.with("correlationId", identifier).and("causationId", event.getId()));
    }

}
