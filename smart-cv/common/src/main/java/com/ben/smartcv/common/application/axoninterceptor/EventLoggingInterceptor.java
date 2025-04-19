package com.ben.smartcv.common.application.axoninterceptor;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.BiFunction;

@Component
@Slf4j
public class EventLoggingInterceptor implements MessageDispatchInterceptor<EventMessage<?>> {

    @NotNull
    @Override
    public BiFunction<Integer, EventMessage<?>, EventMessage<?>> handle(
            @NotNull List<? extends EventMessage<?>> messages) {
        return (index, event) -> {
            log.debug("Publishing event: [{}].", event);
            return event;
        };
    }

}
