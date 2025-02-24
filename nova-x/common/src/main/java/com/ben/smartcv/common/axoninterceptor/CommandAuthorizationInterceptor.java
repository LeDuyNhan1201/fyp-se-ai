package com.ben.novax.common.axoninterceptor;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.InterceptorChain;
import org.axonframework.messaging.MessageHandlerInterceptor;
import org.axonframework.messaging.unitofwork.UnitOfWork;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CommandAuthorizationInterceptor implements MessageHandlerInterceptor<CommandMessage<?>> {

    @Override
    public Object handle(UnitOfWork<? extends CommandMessage<?>> unitOfWork,
                         @NotNull InterceptorChain interceptorChain) throws Exception {
        CommandMessage<?> command = unitOfWork.getMessage();

        log.debug("[LOG] Handling Command: {}", command.getPayloadType().getSimpleName());
        
        // Giả sử có key trong metadata, ta kiểm tra quyền
        String key = (String) command.getMetaData().get("key");
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
