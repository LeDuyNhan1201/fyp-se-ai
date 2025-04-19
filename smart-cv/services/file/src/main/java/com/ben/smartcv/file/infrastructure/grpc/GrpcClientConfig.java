package com.ben.smartcv.file.infrastructure.grpc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Slf4j
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GrpcClientConfig {


}
