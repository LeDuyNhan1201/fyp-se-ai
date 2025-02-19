package com.ben.novax.user.infrastructure;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.gateway.DefaultCommandGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonConfig {

    @Bean
    public CommandGateway commandGateway(CommandBus commandBus) {
        return DefaultCommandGateway.builder()
                .commandBus(commandBus)
                .retryScheduler((commandMessage, lastFailure, failures, commandDispatch) -> false)
                .build();
    }

}
