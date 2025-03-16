package com.ben.smartcv.job.infrastructure;

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
    public ManagedChannel jobServiceManagedChannel() {
        return createChannelForService(31003);
    }

    @Bean
    public JobProcessorGrpc.JobProcessorBlockingStub jobServiceClient(ManagedChannel jobServiceManagedChannel) {
        return JobProcessorGrpc.newBlockingStub(jobServiceManagedChannel);
    }

}
