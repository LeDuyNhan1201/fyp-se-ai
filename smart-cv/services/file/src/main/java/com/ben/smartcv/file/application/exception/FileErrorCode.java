package com.ben.smartcv.file.application.exception;

import lombok.Getter;

@Getter
public enum FileErrorCode {
    NO_FILE_PROVIDED("file/no-file-provided", "ErrorMsg.NoFileProvided"),
    INVALID_FILE_PROVIDED("file/invalid-file-provided", "ErrorMsg.InvalidFileProvided"),
    INVALID_FILE_TYPE("file/invalid-file-type", "ErrorMsg.InvalidFileType"),
    FILE_TOO_LARGE("file/file-too-large", "ErrorMsg.FileTooLarge"),
    CAN_NOT_STORE_FILE("file/can-not-store-file", "ErrorMsg.CanNotStoreFile"),
    COULD_NOT_READ_FILE("file/could-not-read-file", "ErrorMsg.CouldNotReadFile"),
    FILE_NOT_FOUND("file/file-not-found", "ErrorMsg.FileNotFound"),
    CAN_NOT_DELETE_FILE("file/can-not-delete-file", "ErrorMsg.CanNotDeleteFile"),
    CAN_NOT_CHECK_BUCKET("file/can-not-check-bucket", "ErrorMsg.CanNotCheckBucket"),
    CAN_NOT_INIT_BUCKET("file/can-not-init-bucket", "can_not_init_bucket")
    ;

    FileErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private final String code;
    private final String message;

}