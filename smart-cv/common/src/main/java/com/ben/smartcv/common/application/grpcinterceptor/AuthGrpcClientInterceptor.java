package com.ben.smartcv.common.application.grpcinterceptor;

import com.ben.smartcv.common.util.Constant;
import com.ben.smartcv.common.util.StringHelper;
import io.grpc.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.grpc.client.GlobalClientInterceptor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@GlobalClientInterceptor
public class AuthGrpcClientInterceptor implements ClientInterceptor {

    @Value("${spring.application.name}")
    private String applicationName;

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
            MethodDescriptor<ReqT, RespT> method,
            CallOptions callOptions,
            Channel next) {

        String[] methodNameRaw = method.getFullMethodName().split("\\.");
        String methodName = methodNameRaw[methodNameRaw.length - 1];
        log.debug("[{}] client call to [{}]: {}", applicationName,
                StringHelper.convertToUpperHyphen(methodName.split("/")[0]), methodName);

        return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(next.newCall(method, callOptions)) {

            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                String token = (Constant.GRPC_AUTHORIZATION_CONTEXT.get() != null)
                        ? Constant.GRPC_AUTHORIZATION_CONTEXT.get() : Constant.REST_AUTHORIZATION_CONTEXT.get();

                log.debug("Bearer Token: {}", token);
                if (token != null) headers.put(Constant.AUTHORIZATION_KEY, token);

                super.start(responseListener, headers);
            }

            @Override
            public void sendMessage(ReqT message) {
                super.sendMessage(message);
            }

        };
    }

}
