package com.ben.smartcv.notification.application.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationHttpException extends RuntimeException {

    public NotificationHttpException(NotificationError error, HttpStatus httpStatus, String... moreInfo) {
        super(error.getMessage());
        this.httpStatus = httpStatus;
        this.error = error;
        this.moreInfo = moreInfo;
    }

    final String[] moreInfo;
    final HttpStatus httpStatus;
    final NotificationError error;

}