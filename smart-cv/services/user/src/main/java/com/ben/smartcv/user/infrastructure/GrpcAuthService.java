package com.ben.smartcv.user.infrastructure;

import com.ben.smartcv.common.application.exception.CommonHttpException;
import com.ben.smartcv.common.auth.*;
import com.ben.smartcv.user.application.usecase.IAuthenticationUseCase;
import com.ben.smartcv.user.application.usecase.IUserUseCase;
import com.ben.smartcv.user.domain.model.User;
import com.nimbusds.jose.JOSEException;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GrpcAuthService extends AuthServiceGrpc.AuthServiceImplBase {

    IAuthenticationUseCase authenticationUseCase;

    IUserUseCase userUseCase;

    @Override
    public void introspect(IntrospectRequest request, StreamObserver<IntrospectResponse> responseObserver) {
        // Get the token from the request
        String token = request.getToken();

        // Initialize response builder
        IntrospectResponse.Builder responseBuilder = IntrospectResponse.newBuilder();

        try {
            boolean isValid = authenticationUseCase.introspect(token);
            responseBuilder.setValid(isValid);

        } catch (JOSEException | ParseException e) {
            responseObserver.onError(io.grpc.Status.INTERNAL
                    .withDescription(String.format("Token parsing or validation error: %s", e.getMessage()))
                    .asRuntimeException());
            return;
        }

        // Send the response
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void getPreviewById(UserId request, StreamObserver<PreviewUser> responseObserver) {
        try {
            User user = userUseCase.findById(request.getId());
            PreviewUser.Builder responseBuilder = PreviewUser.newBuilder()
                    .setEmail(user.getEmail())
                    .setName(user.getFirstName() + " " + user.getLastName());

            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();
        }
        catch (CommonHttpException exception) {
            log.error("Error while getting user preview: {}", exception.getMessage());
            responseObserver.onError(Status.NOT_FOUND.withDescription("User not found.").asRuntimeException());
        }
    }

}
