package com.ben.smartcv.user.application.handler;

import com.ben.smartcv.common.contract.command.UserCommand;
import com.ben.smartcv.common.util.LogHelper;
import com.ben.smartcv.user.application.dto.ResponseDto;
import com.ben.smartcv.user.application.usecase.IAuthenticationUseCase;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.messaging.annotation.MetaDataValue;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.springframework.stereotype.Component;

import java.text.ParseException;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class UserCommandHandler {

    IAuthenticationUseCase authenticationUseCase;

    @CommandHandler
    public ResponseDto.SignIn on(UserCommand.SignIn command,
                                 @MetaDataValue("correlationId") String correlationId,
                                 @MetaDataValue("causationId") String causationId) {
        LogHelper.logMessage(log, "SignIn", correlationId, causationId, command);
        ResponseDto.SignIn response = authenticationUseCase.signIn(command);
        return response;
    }

    @CommandHandler
    public ResponseDto.Tokens on(UserCommand.Refresh command,
                                 @MetaDataValue("correlationId") String correlationId,
                                 @MetaDataValue("causationId") String causationId) throws ParseException, JOSEException {
        LogHelper.logMessage(log, "Refresh", correlationId, causationId, command);
        ResponseDto.Tokens response = authenticationUseCase.refresh(command);
        return response;
    }

    @ExceptionHandler(payloadType = UserCommand.SignIn.class)
    public void handleExceptionForSignInCommand(Exception exception) {
        log.error("Unexpected exception occurred when signing in: {}", exception.getMessage());
    }

    @ExceptionHandler(payloadType = UserCommand.Refresh.class)
    public void handleExceptionForRefreshCommand(Exception exception) {
        log.error("Unexpected exception occurred when refreshing token: {}", exception.getMessage());
    }

}
