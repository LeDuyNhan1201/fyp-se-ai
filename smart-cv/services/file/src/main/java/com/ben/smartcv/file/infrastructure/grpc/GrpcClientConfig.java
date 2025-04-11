package com.ben.smartcv.file.infrastructure.grpc;

import com.ben.smartcv.common.job.JobServiceGrpc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;

@Slf4j
@Configuration
public class GrpcClientConfig {

    @Bean
    public JobServiceGrpc.JobServiceBlockingStub jobServiceClient(GrpcChannelFactory channels) {
        return JobServiceGrpc.newBlockingStub(channels.createChannel("job-service"));
    }

}
