package com.ben.smartcv.notification.infrastructure.grpc;

import com.ben.smartcv.common.auth.AuthServiceGrpc;
import com.ben.smartcv.common.auth.PreviewUser;
import com.ben.smartcv.common.auth.UserId;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class GrpcClientAuthService {

    AuthServiceGrpc.AuthServiceBlockingStub authServiceClient;

    public PreviewUser callGetById(String userId) {
        return authServiceClient.getPreviewById(UserId.newBuilder().setId(userId).build());
    }

}
