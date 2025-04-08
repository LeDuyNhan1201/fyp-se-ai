package com.ben.smartcv.user.application.projection;

import com.ben.smartcv.common.application.exception.CommonError;
import com.ben.smartcv.common.application.exception.CommonHttpException;
import com.ben.smartcv.common.contract.query.UserQuery;
import com.ben.smartcv.user.application.dto.ResponseDto;
import com.ben.smartcv.user.application.usecase.IAuthenticationUseCase;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.text.ParseException;

import static lombok.AccessLevel.PRIVATE;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class UserProjection {

    IAuthenticationUseCase authenticationUseCase;

    @QueryHandler
    public ResponseDto.Tokens handle(UserQuery.SignIn query) {
        return authenticationUseCase.signIn(query);
    }

    @QueryHandler
    public ResponseDto.Tokens handle(UserQuery.Refresh query) {
        try {
            return authenticationUseCase.refresh(query);
            
        } catch (Exception e) {
            log.error("Error while refreshing token: {}", e.getMessage());
            throw new CommonHttpException(CommonError.TOKEN_INVALID, HttpStatus.UNAUTHORIZED);
        }
    }

}
