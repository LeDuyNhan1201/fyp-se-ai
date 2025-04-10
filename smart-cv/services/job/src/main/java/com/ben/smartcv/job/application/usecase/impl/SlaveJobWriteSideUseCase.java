package com.ben.smartcv.job.application.usecase.impl;

import com.ben.smartcv.common.contract.command.JobCommand;
import com.ben.smartcv.common.contract.command.NotificationCommand;
import com.ben.smartcv.job.application.usecase.ISlaveJobWriteSideUseCase;
import com.ben.smartcv.job.domain.entity.SlaveJob;
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

    ISlaveJobRepository repository;

    @Override
    public void create(SlaveJob item) {
        try {
            repository.save(item);
        } catch (Exception e) {
            log.error("Error creating master job: {}", e.getMessage());
            rollback(item.getId());
        }
    }

    @Override
    public void update(SlaveJob item) {
        log.info("Updating master job: {}", item);
    }

    @Override
    public void delete(String id) {
        log.info("Deleting master job: {}", id);
    }

    private void rollback(String jobId) {
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
