package com.ben.smartcv.job.infrastructure.grpc;

import com.ben.smartcv.common.job.JobProcessorGrpc;
import com.ben.smartcv.common.job.JobServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.Server;
import io.grpc.netty.NettyServerBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

import static com.ben.smartcv.common.util.GrpcHelper.createChannelForService;

@Slf4j
@Configuration
public class GrpcClientConfig {

    @Bean
    public ManagedChannel jobProcessorManagedChannel() {
        return createChannelForService(32003);
    }

    @Bean
    public JobProcessorGrpc.JobProcessorBlockingStub jobProcessorClient(ManagedChannel jobProcessorManagedChannel) {
        return JobProcessorGrpc.newBlockingStub(jobProcessorManagedChannel);
    }

}
