package com.ben.smartcv.job.infrastructure.grpc;

import com.ben.smartcv.common.job.ExtractedJobData;
import com.ben.smartcv.common.job.JobProcessorGrpc;
import com.ben.smartcv.common.job.ProcessJobCommand;
import io.grpc.ManagedChannel;
import jakarta.annotation.PreDestroy;
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

    ManagedChannel jobProcessorManagedChannel;

    JobProcessorGrpc.JobProcessorBlockingStub jobProcessorClient;

    public ExtractedJobData callExtractData(String jobId) {
        ProcessJobCommand request = ProcessJobCommand.newBuilder()
                .setJobId(jobId)
                .build();
        ExtractedJobData response = jobProcessorClient.extractData(request);
        log.info("Extracted job data: {}", response);
        return response;
    }

    @PreDestroy
    public void shutdownGrpcChanel() {
        jobProcessorManagedChannel.shutdown();
    }
}
