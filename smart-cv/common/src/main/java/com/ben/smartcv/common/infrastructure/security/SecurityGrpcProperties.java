package com.ben.smartcv.common.infrastructure.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "security.grpc")
@Getter
@Setter
public class SecurityGrpcProperties {
    private List<String> publicMethods;
}
