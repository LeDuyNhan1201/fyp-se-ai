package com.ben.smartcv.notification.application.exception;

import lombok.Getter;

@Getter
public enum NotificationError {

    CAN_NOT_SEND_MAIL("notification/can-not-send-mail", "ErrorMsg.CanNotSendMail"),
    INVALID_TYPE("notification/invalid-type", "ErrorMsg.InvalidType"),
    ;

    NotificationError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private final String code;
    private final String message;

}