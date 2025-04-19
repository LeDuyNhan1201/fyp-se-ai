package com.ben.smartcv.common.util;

import com.ben.smartcv.common.application.grpcinterceptor.AuthGrpcClientInterceptor;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public final class GrpcHelper {

    public static ManagedChannel createChannelForService(Integer port) {
        return ManagedChannelBuilder.forAddress("localhost", port)
                .usePlaintext()
                .intercept(new AuthGrpcClientInterceptor())
                .build();
    }

}
