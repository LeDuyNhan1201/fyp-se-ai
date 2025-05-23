package com.ben.smartcv.curriculum_vitae.infrastructure.grpc;

import com.ben.smartcv.common.job.JobId;
import com.ben.smartcv.common.job.JobServiceGrpc;
import com.ben.smartcv.common.job.PreviewJobDescription;
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

    JobServiceGrpc.JobServiceBlockingStub jobServiceClient;

    public PreviewJobDescription callGetById(String jobId) {
        PreviewJobDescription response = jobServiceClient.getById(JobId.newBuilder().setId(jobId).build());
        log.info("Preview job: {}", response);
        return response;
    }

}
