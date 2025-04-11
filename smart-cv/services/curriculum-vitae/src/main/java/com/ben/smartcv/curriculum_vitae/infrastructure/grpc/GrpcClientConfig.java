package com.ben.smartcv.curriculum_vitae.infrastructure.grpc;

import com.ben.smartcv.common.cv.CvProcessorGrpc;
import com.ben.smartcv.common.job.JobServiceGrpc;
import io.grpc.ManagedChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;

import static com.ben.smartcv.common.util.GrpcHelper.createChannelForService;

@Slf4j
@Configuration
public class GrpcClientConfig {

    @Bean
    public ManagedChannel cvProcessorManagedChannel() {
        return createChannelForService(31001);
    }

    @Bean
    public CvProcessorGrpc.CvProcessorBlockingStub cvProcessorClient(ManagedChannel cvProcessorManagedChannel) {
        return CvProcessorGrpc.newBlockingStub(cvProcessorManagedChannel);
    }

    @Bean
    public JobServiceGrpc.JobServiceBlockingStub jobServiceClient(GrpcChannelFactory channels) {
        return JobServiceGrpc.newBlockingStub(channels.createChannel("job-service"));
    }

}
