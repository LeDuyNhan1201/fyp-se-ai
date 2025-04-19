package com.ben.smartcv.notification.infrastructure.grpc;

import com.ben.smartcv.common.job.JobId;
import com.ben.smartcv.common.job.JobInfo;
import com.ben.smartcv.common.job.JobServiceGrpc;
import com.ben.smartcv.common.job.PreviewJobDescription;
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
public class GrpcClientJobService {

    ManagedChannel jobServiceManagedChannel;

    JobServiceGrpc.JobServiceBlockingStub jobServiceClient;

    public PreviewJobDescription callGetById(String jobId) {
        PreviewJobDescription response = jobServiceClient.getById(JobId.newBuilder().setId(jobId).build());
        log.info("Preview job: {}", response);
        return response;
    }

    public JobInfo callGetInfoById(String jobId) {
        return jobServiceClient.getInfoById(JobId.newBuilder().setId(jobId).build());
    }

    @PreDestroy
    public void shutdownGrpcChanel() {
        jobServiceManagedChannel.shutdown();
    }

}
