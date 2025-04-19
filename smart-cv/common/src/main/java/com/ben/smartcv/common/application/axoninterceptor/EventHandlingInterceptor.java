package com.ben.smartcv.common.application.axoninterceptor;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventMessage;
import org.axonframework.messaging.InterceptorChain;
import org.axonframework.messaging.MessageHandlerInterceptor;
import org.axonframework.messaging.unitofwork.UnitOfWork;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EventHandlingInterceptor implements MessageHandlerInterceptor<EventMessage<?>> {

    @Override
    public Object handle(UnitOfWork<? extends EventMessage<?>> unitOfWork,
                         @NotNull InterceptorChain interceptorChain) throws Exception {
        EventMessage<?> event = unitOfWork.getMessage();

        log.debug("[LOG] Handling Event: {}", event.getPayloadType().getSimpleName());

        // Giả sử có key trong metadata, ta kiểm tra quyền
        String key = (String) event.getMetaData().get("key");
        if (key == null || !key.equals("123")) {
            throw new SecurityException("Unauthorized user");
        }

        long startTime = System.currentTimeMillis();
        Object result = interceptorChain.proceed();
        long elapsedTime = System.currentTimeMillis() - startTime;

        log.debug("[LOG] Command processed in {} ms", elapsedTime);

        return result;
    }

}
