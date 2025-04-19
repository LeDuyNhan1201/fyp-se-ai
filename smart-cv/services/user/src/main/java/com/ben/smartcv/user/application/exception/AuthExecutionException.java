package com.ben.smartcv.user.application.exception;

import org.axonframework.commandhandling.CommandExecutionException;

public class AuthExecutionException extends CommandExecutionException {

    public AuthExecutionException(AuthError error, Throwable cause, String... moreInfo) {
        super(error.getMessage(), cause);
        this.error = error;
        this.cause = cause;
        this.moreInfo = moreInfo;
    }

    final String[] moreInfo;
    final Throwable cause;
    final AuthError error;

}
