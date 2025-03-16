package com.ben.smartcv.common.util;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GrpcHelper {

    public static ManagedChannel createChannelForService(Integer port) {
        return ManagedChannelBuilder.forAddress("localhost", port)
                .usePlaintext()
//                .intercept(new GrpcClientInterceptor())
                .build();
    }

}
