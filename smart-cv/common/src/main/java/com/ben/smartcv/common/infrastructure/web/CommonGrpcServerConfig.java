package com.ben.smartcv.common.infrastructure.web;

import io.grpc.health.v1.HealthCheckResponse;
import io.grpc.protobuf.services.HealthStatusManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class CommonGrpcServerConfig {

    @Value("${spring.grpc.server.name}")
    private String serviceName;

    @Bean
    public HealthStatusManager healthStatusManager() {
        HealthStatusManager manager = new HealthStatusManager();
        manager.setStatus(serviceName, HealthCheckResponse.ServingStatus.SERVING);
        return manager;
    }

}
