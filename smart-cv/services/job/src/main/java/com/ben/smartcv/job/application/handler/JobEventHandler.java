package com.ben.smartcv.job.application.handler;

import com.ben.smartcv.common.contract.command.JobCommand;
import com.ben.smartcv.common.contract.event.JobEvent;
import com.ben.smartcv.common.job.ExtractedJobData;
import com.ben.smartcv.job.domain.entity.SlaveJob;
import com.ben.smartcv.job.infrastructure.EventPublisher;
import com.ben.smartcv.job.infrastructure.grpc.GrpcClientJobProcessor;
import com.ben.smartcv.job.infrastructure.elasticsearch.IJobRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.data.domain.Range;
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

    GrpcClientJobProcessor grpcClientJobProcessor;

    IJobRepository jobRepository;

    @EventHandler
    public void on(JobEvent.JobCreated event) {
        Range<Double> salaryRange = Range.closed(event.getFromSalary(), event.getToSalary());
        SlaveJob slaveJob = SlaveJob.builder()
                .id(event.getJobId())
                .organizationName(event.getOrganizationName())
                .position(event.getPosition())
                .salary(salaryRange)
                .expiredAt(event.getExpiredAt())
                .rawText(event.getRequirements())
                .build();

        jobRepository.save(slaveJob);
        eventPublisher.send(event);
    }


    @EventHandler
    public void on(JobEvent.JobProcessed event) {
        try {
            ExtractedJobData extractedJobData = grpcClientJobProcessor.callExtractData(event.getJobId());
            SlaveJob currentSlaveJob = jobRepository.findById(event.getJobId()).orElse(null);

            if (currentSlaveJob == null) {
                log.error("Job not found: {}", event.getJobId());

            } else {
                currentSlaveJob.setEmail(extractedJobData.getEmail());
                currentSlaveJob.setPhone(extractedJobData.getPhone());
                currentSlaveJob.setEducations(extractedJobData.getEducationsList());
                currentSlaveJob.setSkills(extractedJobData.getSkillsList());
                currentSlaveJob.setExperiences(extractedJobData.getExperiencesList());

                try {
                    jobRepository.save(currentSlaveJob);

                } catch (Exception e) {
                    log.error("Error saving job: {}", e.getMessage());
                    sendRollbackCommand(event.getJobId());
                }
            }

        } catch (Exception e) {
            log.error(String.valueOf(e));
            log.error("Extract data failed: {}", e.getMessage());
            sendRollbackCommand(event.getJobId());

        }
        eventPublisher.send(event);
    }

    @EventHandler
    public void on(JobEvent.JobDeleted event) {
        jobRepository.deleteById(event.getJobId());
        eventPublisher.send(event);
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
