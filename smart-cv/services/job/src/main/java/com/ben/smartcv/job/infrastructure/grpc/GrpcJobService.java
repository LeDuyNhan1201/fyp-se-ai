package com.ben.smartcv.job.infrastructure.grpc;

import com.ben.smartcv.common.job.JobId;
import com.ben.smartcv.common.job.JobServiceGrpc;
import com.ben.smartcv.common.job.PreviewJobDescription;
import com.ben.smartcv.job.domain.model.SlaveJob;
import com.ben.smartcv.job.infrastructure.repository.ISlaveJobRepository;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class GrpcJobService extends JobServiceGrpc.JobServiceImplBase {

    ISlaveJobRepository slaveJobRepository;

    @Override
    public void getById(JobId request, StreamObserver<PreviewJobDescription> responseObserver) {
        Optional<SlaveJob> job = slaveJobRepository.findById(request.getId());

        if (job.isEmpty()) {
            responseObserver.onError(Status.NOT_FOUND.withDescription("Job not found.").asRuntimeException());
            return;
        }

        PreviewJobDescription.Builder responseBuilder = PreviewJobDescription.newBuilder()
                .addAllSkills(job.get().getSkills())
                .addAllEducations(job.get().getEducations())
                .addAllExperiences(job.get().getExperiences());

        // Send the response
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

}
