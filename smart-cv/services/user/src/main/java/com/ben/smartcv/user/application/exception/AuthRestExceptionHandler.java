package com.ben.smartcv.user.application.exception;

import com.ben.smartcv.common.contract.dto.BaseResponse;
import com.ben.smartcv.common.util.LogHelper;
import com.ben.smartcv.common.util.Translator;
import com.ben.smartcv.user.adapter.CommandController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice(assignableTypes = { CommandController.class })
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class AuthRestExceptionHandler {

    @Value("${spring.application.name}")
    private String microserviceName;

    @ExceptionHandler(value = AuthHttpException.class)
    public ResponseEntity<?> handle(AuthHttpException exception) {
        LogHelper.logError(log, exception.getMessage(), exception);
        AuthError error = exception.getError();
        return ResponseEntity.status(exception.getHttpStatus()).body(BaseResponse.builder()
                .errorCode(error.getCode())
                .message((exception.getMoreInfo() != null)
                        ? Translator.getMessage(error.getMessage(), exception.getMoreInfo())
                        : Translator.getMessage(error.getMessage()))
                .errors(switch (error) {
                    case WRONG_PASSWORD -> new HashMap<>(Map.of(
                            "password", Translator.getMessage(error.getMessage()))
                    );
                    case PASSWORD_MIS_MATCH -> new HashMap<>(Map.of(
                            "password", Translator.getMessage(error.getMessage()),
                            "confirmationPassword", Translator.getMessage(error.getMessage()))
                    );
                    case TERMS_NOT_ACCEPTED -> new HashMap<>(Map.of(
                            "acceptTerms", Translator.getMessage(error.getMessage()))
                    );

                    default -> null;
                })
                .build());
    }

}