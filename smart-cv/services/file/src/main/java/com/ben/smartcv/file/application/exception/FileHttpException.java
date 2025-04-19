package com.ben.smartcv.file.application.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@FieldDefaults(level = AccessLevel.PROTECTED)
public class FileHttpException extends RuntimeException {

    public FileHttpException(String Message, HttpStatus httpStatus, String... moreInfo) {
        super(Message);
        this.httpStatus = httpStatus;
        this.moreInfo = moreInfo;
    }

    public FileHttpException(FileError error, HttpStatus httpStatus, String... moreInfo) {
        super(error.getMessage());
        this.httpStatus = httpStatus;
        this.error = error;
        this.moreInfo = moreInfo;
    }

    @Setter
    String[] moreInfo;
    final HttpStatus httpStatus;
    FileError error;

}