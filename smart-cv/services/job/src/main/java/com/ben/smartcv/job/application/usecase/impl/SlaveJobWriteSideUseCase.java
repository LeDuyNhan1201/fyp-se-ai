package com.ben.smartcv.job.application.usecase.impl;

import com.ben.smartcv.common.contract.command.JobCommand;
import com.ben.smartcv.common.contract.command.NotificationCommand;
import com.ben.smartcv.job.application.usecase.ISlaveJobWriteSideUseCase;
import com.ben.smartcv.job.domain.model.MasterJob;
import com.ben.smartcv.job.domain.model.SlaveJob;
import com.ben.smartcv.job.infrastructure.repository.IMasterJobRepository;
import com.ben.smartcv.job.infrastructure.repository.ISlaveJobRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.MetaData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Transactional
public class SlaveJobWriteSideUseCase implements ISlaveJobWriteSideUseCase {

    CommandGateway commandGateway;

    ISlaveJobRepository slaveJobRepository;

    IMasterJobRepository masterJobRepository;

    @Override
    public void create(SlaveJob item) {
        if (slaveJobRepository.existsById(item.getId())) return;
        try {
            slaveJobRepository.save(item);
        } catch (Exception e) {
            log.error("Error creating master job: {}", e.getMessage());
            rollbackCreate(item.getId());
        }
    }

    @Override
    public void update(SlaveJob item) {
        log.info("Updating master job: {}", item);
    }

    @Override
    public void delete(String id) {
        try {
            slaveJobRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Error deleting slave job: {}", e.getMessage());
            SlaveJob slaveJob = slaveJobRepository.findById(id).orElse(null);
            if (slaveJob != null) {
                MasterJob masterJob = MasterJob.builder()
                        .id(slaveJob.getId())
                        .createdBy(slaveJob.getCreatedBy())
                        .createdAt(slaveJob.getCreatedAt())
                        .updatedBy(slaveJob.getUpdatedBy())
                        .updatedAt(slaveJob.getUpdatedAt())
                        .isDeleted(slaveJob.isDeleted())
                        .deletedBy(slaveJob.getDeletedBy())
                        .deletedAt(slaveJob.getDeletedAt())
                        .version(slaveJob.getVersion())
                        .organizationName(slaveJob.getOrganizationName())
                        .email(slaveJob.getEmail())
                        .phone(slaveJob.getPhone())
                        .position(slaveJob.getPosition())
                        .educations(slaveJob.getEducations())
                        .skills(slaveJob.getSkills())
                        .experiences(slaveJob.getExperiences())
                        .fromSalary(slaveJob.getSalary().getLowerBound().getValue().get())
                        .toSalary(slaveJob.getSalary().getUpperBound().getValue().get())
                        .expiredAt(slaveJob.getExpiredAt())
                        .requirements(slaveJob.getRequirements())
                        .build();
                masterJobRepository.save(masterJob);
            }
        }
    }

    private void rollbackCreate(String jobId) {
        String identifier = UUID.randomUUID().toString();
        commandGateway.send(JobCommand.RollbackCreateJob.builder()
                .id(identifier)
                .jobId(jobId)
                .build(), MetaData.with("correlationId", identifier).and("causationId", jobId));

        identifier = UUID.randomUUID().toString();
        commandGateway.sendAndWait(NotificationCommand.SendNotification.builder()
                .id(identifier)
                .title("Notify.Title.CreateFailed|Job")
                .content("Notify.Content.CreateFailed|Job")
                .build(), MetaData.with("correlationId", identifier).and("causationId", jobId));
    }

}
