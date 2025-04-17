package com.ben.smartcv.common.application.restinterceptor;

import com.ben.smartcv.common.util.Constant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class BearerTokenInterceptor implements HandlerInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
        
        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)) {
            String uuid = SecurityContextHolder.getContext().getAuthentication().getName();
            Constant.REST_AUTHORIZATION_CONTEXT.set(uuid);
            log.info("Bearer Token: {}", Constant.REST_AUTHORIZATION_CONTEXT.get());
        }
        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request,
                                @NonNull HttpServletResponse response, @NonNull Object handler, Exception ex) {
        // Clean up ThreadLocal after request is complete
    }

}