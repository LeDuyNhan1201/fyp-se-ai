package com.ben.smartcv.job.application.usecase;

import com.ben.smartcv.common.contract.command.JobCommand;
import com.ben.smartcv.common.contract.command.NotificationCommand;
import com.ben.smartcv.job.domain.entity.SlaveJob;
import com.ben.smartcv.job.infrastructure.repository.ISlaveJobRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
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
        commandGateway.send(JobCommand.RollbackCreateJob.builder()
                .id(UUID.randomUUID().toString())
                .jobId(jobId)
                .build());

        commandGateway.sendAndWait(NotificationCommand.SendNotification.builder()
                .id(UUID.randomUUID().toString())
                .title("Notify.Title.CreateFailed")
                .content("Notify.Content.CreateFailed")
                .build());
    }

}
