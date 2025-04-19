package com.ben.smartcv.notification.infrastructure.grpc;

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
    public ManagedChannel jobServiceManagedChannel(GrpcChannelFactory channelFactory) {
        return channelFactory.createChannel("job-service");
    }

    @Bean
    public JobServiceGrpc.JobServiceBlockingStub jobServiceClient(ManagedChannel jobServiceManagedChannel) {
        return JobServiceGrpc.newBlockingStub(jobServiceManagedChannel);
    }

}
