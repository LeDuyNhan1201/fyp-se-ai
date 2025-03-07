package com.ben.smartcv.file.application.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@FieldDefaults(level = AccessLevel.PROTECTED)
public class FileException extends RuntimeException {

    public FileException(String Message, HttpStatus httpStatus, String... moreInfo) {
        super(Message);
        this.httpStatus = httpStatus;
        this.moreInfo = moreInfo;
    }

    public FileException(FileErrorCode errorCode, HttpStatus httpStatus, String... moreInfo) {
        super(errorCode.getMessage());
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.moreInfo = moreInfo;
    }

    @Setter
    String[] moreInfo;
    final HttpStatus httpStatus;
    FileErrorCode errorCode;

}