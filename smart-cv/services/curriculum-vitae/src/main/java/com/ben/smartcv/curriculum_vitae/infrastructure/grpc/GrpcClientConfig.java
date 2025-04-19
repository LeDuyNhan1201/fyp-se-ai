package com.ben.smartcv.curriculum_vitae.infrastructure.grpc;

import com.ben.smartcv.common.cv.CvProcessorGrpc;
import com.ben.smartcv.common.file.FileServiceGrpc;
import com.ben.smartcv.common.job.JobServiceGrpc;
import io.grpc.ManagedChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;

@Slf4j
@Configuration
public class GrpcClientConfig {

    @Bean
    public CvProcessorGrpc.CvProcessorBlockingStub cvProcessorClient(GrpcChannelFactory channelFactory) {
        ManagedChannel channel = channelFactory.createChannel("cv-processor");
        return CvProcessorGrpc.newBlockingStub(channel);
    }

    @Bean
    public JobServiceGrpc.JobServiceBlockingStub jobServiceClient(GrpcChannelFactory channelFactory) {
        ManagedChannel channel = channelFactory.createChannel("job-service");
        return JobServiceGrpc.newBlockingStub(channel);
    }

    @Bean
    public FileServiceGrpc.FileServiceBlockingStub fileServiceClient(GrpcChannelFactory channelFactory) {
        ManagedChannel channel = channelFactory.createChannel("file-service");
        return FileServiceGrpc.newBlockingStub(channel);
    }

}
