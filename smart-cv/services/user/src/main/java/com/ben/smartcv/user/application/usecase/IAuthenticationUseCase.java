package com.ben.smartcv.user.application.usecase;

import com.ben.smartcv.common.contract.event.UserEvent;
import com.ben.smartcv.common.contract.query.UserQuery;
import com.ben.smartcv.user.application.dto.RequestDto;
import com.ben.smartcv.user.application.dto.ResponseDto;
import com.nimbusds.jose.JOSEException;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
public interface IAuthenticationUseCase {

    boolean introspect(String token) throws JOSEException, ParseException;

    void validateSignUp(RequestDto.SignUp request);

    void signUp(UserEvent.UserSignedUp event);

    ResponseDto.Tokens signIn(UserQuery.SignIn query);

    ResponseDto.Tokens refresh(UserQuery.Refresh query) throws ParseException, JOSEException;

    void signOut(String accessToken, String refreshToken) throws ParseException, JOSEException;

}