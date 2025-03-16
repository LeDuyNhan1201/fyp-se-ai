package com.ben.smartcv.job.application.handler;

import com.ben.smartcv.common.contract.command.JobCommand;
import com.ben.smartcv.common.contract.event.JobEvent;
import com.ben.smartcv.common.job.ExtractedJobData;
import com.ben.smartcv.job.domain.entity.Job;
import com.ben.smartcv.job.infrastructure.EventPublisher;
import com.ben.smartcv.job.infrastructure.GrpcClientJobService;
import com.ben.smartcv.job.infrastructure.IJobRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class JobEventHandler {

    EventPublisher eventPublisher;

    CommandGateway commandGateway;

    GrpcClientJobService grpcClientJobService;

    IJobRepository jobRepository;

    @EventHandler
    public void on(JobEvent.JobCreated event) {
        Job job = Job.builder()
                .id(event.getJobId())
                .organizationName(event.getOrganizationName())
                .position(event.getPosition())
                .rawText(event.getRequirements())
                .build();

        jobRepository.save(job);
        eventPublisher.send(event);
    }


    @EventHandler
    public void on(JobEvent.JobProcessed event) {
        try {
            ExtractedJobData extractedJobData = grpcClientJobService.callExtractData(event.getJobId());
            Job currentJob = jobRepository.findById(event.getJobId()).orElse(null);

            if (currentJob == null) {
                log.error("Job not found: {}", event.getJobId());

            } else {
                currentJob.setEmail(extractedJobData.getEmail());
                currentJob.setPhone(extractedJobData.getPhone());
                currentJob.setEducation(extractedJobData.getEducation());
                currentJob.setSkills(extractedJobData.getSkills());
                currentJob.setExperience(extractedJobData.getExperience());

                try {
                    jobRepository.save(currentJob);

                } catch (Exception e) {
                    log.error("Error saving job: {}", e.getMessage());
                }
            }

        } catch (Exception e) {
            log.error("Extract data failed: {}", e.getMessage());
            sendRollbackCommand(event.getJobId());

        }
        eventPublisher.send(event);
    }

    @EventHandler
    public void on(JobEvent.JobDeleted event) {
        jobRepository.deleteById(event.getJobId());
        eventPublisher.send(event);
        log.info("Job deleted event: {}", event.getJobId());
    }

    public void handleExceptionForJobDeletedEvent(Exception exception, JobEvent.JobDeleted event) {
        log.error("Unexpected exception occurred when deleting job {}: {}", event.getJobId(), exception.getMessage());
    }

    private void sendRollbackCommand(String jobId) {
        commandGateway.send(JobCommand.RollbackProcessJob.builder()
                .id(UUID.randomUUID().toString())
                .jobId(jobId)
                .build());
    }

}
