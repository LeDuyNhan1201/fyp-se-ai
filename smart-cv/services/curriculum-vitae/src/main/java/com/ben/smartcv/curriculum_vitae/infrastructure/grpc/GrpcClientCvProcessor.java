package com.ben.smartcv.curriculum_vitae.infrastructure.grpc;

import com.ben.smartcv.common.contract.event.CvEvent;
import com.ben.smartcv.common.cv.CvProcessedEvent;
import com.ben.smartcv.common.cv.CvProcessorGrpc;
import com.ben.smartcv.common.cv.ExtractedCvData;
import com.ben.smartcv.common.cv.RawCvInfo;
import com.ben.smartcv.common.job.JobId;
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
public class GrpcClientCvProcessor {

    ManagedChannel cvProcessorManagedChannel;

    CvProcessorGrpc.CvProcessorBlockingStub cvProcessorClient;

    JobServiceGrpc.JobServiceBlockingStub jobServiceClient;

    public ExtractedCvData callExtractData(CvEvent.CvProcessed event) {
        PreviewJobDescription previewJobDescription = jobServiceClient.getById(
                JobId.newBuilder().setId(event.getJobId()).build());

        RawCvInfo rawCvInfo = RawCvInfo.newBuilder()
                .setObjectKey(event.getObjectKey())
                .setPreviewJob(PreviewJobDescription.newBuilder()
                        .addAllEducations(previewJobDescription.getEducationsList())
                        .addAllSkills(previewJobDescription.getSkillsList())
                        .addAllExperiences(previewJobDescription.getExperiencesList())
                        .build())
                .build();
        ExtractedCvData response = cvProcessorClient.extractData(rawCvInfo);
        log.info("Extracted cv data: {}", response);
        return response;
    }

    @PreDestroy
    public void shutdownGrpcChanel() {
        cvProcessorManagedChannel.shutdown();
    }

}
