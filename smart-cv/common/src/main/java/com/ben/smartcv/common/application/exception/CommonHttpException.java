package com.ben.smartcv.common.application.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommonHttpException extends RuntimeException {

    public CommonHttpException(CommonError error, HttpStatus httpStatus, String... moreInfo) {
        super(error.getMessage());
        this.httpStatus = httpStatus;
        this.error = error;
        this.moreInfo = moreInfo;
    }

    final String[] moreInfo;
    final HttpStatus httpStatus;
    final CommonError error;

}