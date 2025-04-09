package com.ben.smartcv.gateway.exception;

import lombok.Getter;

@Getter
public enum AppError {

    TOKEN_MISSING("gateway/token-missing", "ErrorMsg.TokenMissing"),
    FALLBACK_ERROR("gateway/fallback-error", "ErrorMsg.Fallback"),
    ;

    AppError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private final String code;
    private final String message;

}