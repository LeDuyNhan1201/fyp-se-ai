package com.ben.smartcv.job.infrastructure.grpc;

import com.ben.smartcv.common.contract.event.JobEvent;
import com.ben.smartcv.common.job.ExtractedJobData;
import com.ben.smartcv.common.job.JobCreatedEvent;
import com.ben.smartcv.common.job.JobProcessorGrpc;
import com.ben.smartcv.common.util.TimeHelper;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class GrpcClientJobProcessor {

    JobProcessorGrpc.JobProcessorBlockingStub jobProcessorClient;

    public ExtractedJobData callExtractData(JobEvent.JobCreated event) {
        JobCreatedEvent protoEvent = JobCreatedEvent.newBuilder()
                .setOrganizationName(event.getOrganizationName())
                .setPosition(event.getPosition())
                .setExpiredAt(TimeHelper.convertToTimestamp(event.getExpiredAt()))
                .setFromSalary(event.getFromSalary())
                .setToSalary(event.getToSalary())
                .setRequirements(event.getRequirements())
                .build();
        ExtractedJobData response = jobProcessorClient.extractData(protoEvent);
        log.info("Extracted job data: {}", response);
        return response;
    }

}
