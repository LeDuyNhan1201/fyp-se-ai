package com.ben.smartcv.job.application.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JobHttpException extends RuntimeException {

    public JobHttpException(JobError error, HttpStatus httpStatus, String... moreInfo) {
        super(error.getMessage());
        this.httpStatus = httpStatus;
        this.error = error;
        this.moreInfo = moreInfo;
    }

    final String[] moreInfo;
    final HttpStatus httpStatus;
    final JobError error;

}