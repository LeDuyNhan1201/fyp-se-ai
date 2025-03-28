package com.ben.smartcv.job.application.exception;

import com.ben.smartcv.common.component.Translator;
import com.ben.smartcv.common.contract.dto.BaseResponse;
import com.ben.smartcv.common.util.LogHelper;
import com.ben.smartcv.job.adapter.CommandController;
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
public class JobRestExceptionHandler {

    @Value("${spring.application.name}")
    private String microserviceName;

    @ExceptionHandler(value = JobHttpException.class)
    public ResponseEntity<?> handlingJobHttpException(JobHttpException exception) {
        LogHelper.logError(log, exception.getMessage(), exception);
        JobError error = exception.getError();
        return ResponseEntity.status(exception.getHttpStatus()).body(BaseResponse.builder()
                .errorCode(error.getCode())
                .message((exception.getMoreInfo() != null)
                        ? Translator.getMessage(error.getMessage(), exception.getMoreInfo())
                        : Translator.getMessage(error.getMessage()))
                .errors(switch (error) {
                    case INVALID_SALARY_RANGE -> new HashMap<>(Map.of(
                            "fromSalary", Translator.getMessage(error.getMessage()),
                            "toSalary", Translator.getMessage(error.getMessage()))
                    );

                    default -> null;
                })
                .build());
    }

}