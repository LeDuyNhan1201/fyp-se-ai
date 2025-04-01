package com.ben.smartcv.job.infrastructure.grpc;

import com.ben.smartcv.common.job.JobProcessorGrpc;
import io.grpc.ManagedChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.ben.smartcv.common.util.GrpcHelper.createChannelForService;

@Configuration
@Slf4j
public class GrpcClientConfig {

    @Bean
    public ManagedChannel jobProcessorManagedChannel() {
        return createChannelForService(31003);
    }

    @Bean
    public JobProcessorGrpc.JobProcessorBlockingStub jobProcessorClient(ManagedChannel jobProcessorManagedChannel) {
        return JobProcessorGrpc.newBlockingStub(jobProcessorManagedChannel);
    }

}
