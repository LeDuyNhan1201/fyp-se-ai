package com.ben.smartcv.notification.application.exception;

import com.ben.smartcv.common.contract.dto.BaseResponse;
import com.ben.smartcv.common.util.LogHelper;
import com.ben.smartcv.common.util.Translator;
import com.ben.smartcv.notification.adapter.CommandController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = { CommandController.class })
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class NotificationRestExceptionHandler {

    @Value("${spring.application.name}")
    private String microserviceName;

    @ExceptionHandler(value = NotificationHttpException.class)
    public ResponseEntity<?> handlingJobHttpException(NotificationHttpException exception) {
        LogHelper.logError(log, exception.getMessage(), exception);
        NotificationError error = exception.getError();
        return ResponseEntity.status(exception.getHttpStatus()).body(BaseResponse.builder()
                .errorCode(error.getCode())
                .message((exception.getMoreInfo() != null)
                        ? Translator.getMessage(error.getMessage(), exception.getMoreInfo())
                        : Translator.getMessage(error.getMessage()))
                .build());
    }

}