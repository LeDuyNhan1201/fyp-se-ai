package com.ben.smartcv.common.infrastructure.web;

import com.ben.smartcv.common.auth.AuthServiceGrpc;
import io.grpc.ManagedChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;

@Slf4j
@Configuration
public class CommonGrpcClientConfig {

    @Bean
    public AuthServiceGrpc.AuthServiceBlockingStub authServiceClient(GrpcChannelFactory channelFactory) {
        ManagedChannel channel = channelFactory.createChannel("auth-service");
        return AuthServiceGrpc.newBlockingStub(channel);
    }

}
