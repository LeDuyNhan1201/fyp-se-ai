package com.ben.novax.user.application.usecase.axoninterceptor;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.BiFunction;

@Component
@Slf4j
public class CommandLoggingInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {

    @NotNull
    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(
            @NotNull List<? extends CommandMessage<?>> messages) {
        return (index, command) -> {
            log.debug("[LOG] Dispatching Command: {}", command.getPayloadType().getSimpleName());
            
            // Adding logic here to log the command

            return command;
        };
    }

}
