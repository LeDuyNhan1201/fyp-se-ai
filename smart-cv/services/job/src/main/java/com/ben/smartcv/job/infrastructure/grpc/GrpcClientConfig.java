package com.ben.smartcv.job.infrastructure.grpc;

import com.ben.smartcv.common.job.JobProcessorGrpc;
import io.grpc.ManagedChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;

@Slf4j
@Configuration
public class GrpcClientConfig {

    @Bean
    public JobProcessorGrpc.JobProcessorBlockingStub jobProcessorClient(GrpcChannelFactory channelFactory) {
        ManagedChannel channel = channelFactory.createChannel("job-processor");
        return JobProcessorGrpc.newBlockingStub(channel);
    }

}
