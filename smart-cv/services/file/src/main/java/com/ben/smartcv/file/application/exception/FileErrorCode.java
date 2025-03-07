package com.ben.smartcv.file.application.exception;

import lombok.Getter;

@Getter
public enum FileErrorCode {
    NO_FILE_PROVIDED("file/no-file-provided", "no_file_provided"),
    INVALID_FILE_PROVIDED("file/invalid-file-provided", "invalid_file_provided"),
    INVALID_FILE_TYPE("file/invalid-file-type", "invalid_file_type"),
    FILE_TOO_LARGE("file/file-too-large", "file_too_large"),
    CAN_NOT_STORE_FILE("file/can-not-store-file", "can_not_store_file"),
    COULD_NOT_READ_FILE("file/could-not-read-file", "could_not_read_file"),
    FILE_NOT_FOUND("file/file-not-found", "file_not_found"),
    CAN_NOT_DELETE_FILE("file/can-not-delete-file", "can_not_delete_file"),
    CAN_NOT_CHECK_BUCKET("file/can-not-check-bucket", "can_not_check_bucket"),
    CAN_NOT_INIT_BUCKET("file/can-not-init-bucket", "can_not_init_bucket")
    ;

    FileErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private final String code;
    private final String message;

}