package com.ben.smartcv.common.application.grpcinterceptor;

import io.grpc.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.grpc.server.GlobalServerInterceptor;

@GlobalServerInterceptor
@Slf4j
public class GrpcExceptionServerInterceptor implements ServerInterceptor {

    @Value("${spring.application.name}")
    private String microserviceName;

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        ServerCall.Listener<ReqT> delegate = next.startCall(call, headers);
        
        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT>(delegate) {

            @Override
            public void onHalfClose() {
                try {
                    super.onHalfClose();
                } catch (Exception e) {
                    call.close(Status.RESOURCE_EXHAUSTED
                            .withDescription(String.format("[%s]: Exception thrown by application: %s",
                                    microserviceName, e.getMessage())), new Metadata());
                }
            }

            @Override
            public void onCancel() {
                super.onCancel();
                // Handle cancellation if needed
            }

            @Override
            public void onComplete() {
                super.onComplete();
                // Handle completion if needed
            }
        };
    }
}