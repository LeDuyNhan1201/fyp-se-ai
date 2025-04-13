package com.ben.smartcv.user.application.usecase;

import com.ben.smartcv.common.auth.IntrospectRequest;
import com.ben.smartcv.common.auth.IntrospectResponse;
import com.ben.smartcv.common.contract.command.UserCommand;
import com.ben.smartcv.common.contract.event.UserEvent;
import com.ben.smartcv.common.contract.query.UserQuery;
import com.ben.smartcv.user.application.dto.RequestDto;
import com.ben.smartcv.user.application.dto.ResponseDto;
import com.nimbusds.jose.JOSEException;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
public interface IAuthenticationUseCase {

    boolean introspect(String token) throws JOSEException, ParseException;

    void introspect(IntrospectRequest request, StreamObserver<IntrospectResponse> responseObserver);

    void validateSignUp(RequestDto.SignUp request);

    void signUp(UserEvent.SignedUp event);

    ResponseDto.SignIn signIn(UserCommand.SignIn command);

    ResponseDto.Tokens refresh(UserCommand.Refresh command) throws ParseException, JOSEException;

    void signOut(String accessToken, String refreshToken) throws ParseException, JOSEException;

}