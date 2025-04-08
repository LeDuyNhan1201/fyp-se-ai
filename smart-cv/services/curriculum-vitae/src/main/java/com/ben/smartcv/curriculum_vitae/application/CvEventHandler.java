package com.ben.smartcv.curriculum_vitae.application;

import com.ben.smartcv.common.contract.command.CvCommand;
import com.ben.smartcv.common.contract.command.NotificationCommand;
import com.ben.smartcv.common.contract.event.CvEvent;
import com.ben.smartcv.common.cv.ExtractedCvData;
import com.ben.smartcv.common.util.LogHelper;
import com.ben.smartcv.curriculum_vitae.domain.entity.CurriculumVitae;
import com.ben.smartcv.curriculum_vitae.infrastructure.EventPublisher;
import com.ben.smartcv.curriculum_vitae.infrastructure.ICurriculumVitaeRepository;
import com.ben.smartcv.curriculum_vitae.infrastructure.grpc.GrpcClientCvProcessor;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.EventHandler;
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

    ICurriculumVitaeRepository repository;

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
                    .id(event.getCvId())
                    .cvFileName(event.getObjectKey())

                    .name(extractedCvData.getName())
                    .email(extractedCvData.getEmail())
                    .phone(extractedCvData.getPhone())
                    .educations(extractedCvData.getEducationsList())
                    .skills(extractedCvData.getSkillsList())
                    .experiences(extractedCvData.getExperiencesList())
                    .build();
            repository.save(curriculumVitae);

        } catch (Exception e) {
            log.error("Cv processing failed: {}", e.getMessage());
            String reason = "Notify.Content.CreateFailed|CV";
            if (e instanceof StatusRuntimeException) {
                Status status = ((StatusRuntimeException) e).getStatus();
                if (status.getCode() == Status.Code.INVALID_ARGUMENT) {
                    log.error("Invalid argument: {}", status.getDescription());
                    reason = "Notify.Content.InvalidRequirements|CV";
                } else {
                    log.error("Unexpected error: {}", status.getDescription());
                }
            }
            rollback(event, reason);
        }
        eventPublisher.send(event, correlationId, causationId);
    }

    @EventHandler
    public void on(CvEvent.CvDeleted event,
                   @MetaDataValue("correlationId") String correlationId,
                   @MetaDataValue("causationId") String causationId) {
        // 4
        LogHelper.logMessage(log, "4|CvDeleted", correlationId, causationId, event);
        try {
            repository.deleteById(event.getCvId());
            eventPublisher.send(event, correlationId, causationId);

        } catch (Exception exception){
            log.error("Cannot delete cv {}: {}", event.getCvId(), exception.getMessage());
        }
    }

    private void sendFailureNotification(String reason) {
        commandGateway.sendAndWait(NotificationCommand.SendNotification.builder()
                .id(UUID.randomUUID().toString())
                .title("Notify.Title.CreateFailed|CV")
                .content(reason)
                .build());
    }

    private void rollback(CvEvent.CvProcessed event, String reason) {
        commandGateway.send(CvCommand.RollbackProcessCv.builder()
                .id(UUID.randomUUID().toString())
                .cvId(event.getCvId())
                .objectKey(event.getObjectKey())
                .build());

        sendFailureNotification(reason);
    }

}
