package com.ben.smartcv.common.exception;

import lombok.Getter;

@Getter
public enum CommonError {
    // Validation Errors
    VALIDATION_ERROR("common/validation-error", "ErrorMsg.ValidationError "),

    // Token Errors
    TOKEN_MISSING("common/token-missing", "ErrorMsg.TokenMissing"),
    TOKEN_INVALID("common/token-invalid", "ErrorMsg.TokenInvalid"),
    TOKEN_EXPIRED("common/token-expired", "ErrorMsg.TokenExpired"),
    TOKEN_REVOKED("common/token-revoked", "ErrorMsg.TokenRevoked"),
    SIGNATURE_INVALID("auth/signature-invalid", "ErrorMsg.SignatureInvalid"),

    //Rate Limiting Errors
    TOO_MANY_REQUESTS("common/too-many-requests", "ErrorMsg.TooManyRequests"),
    RATE_LIMIT_EXCEEDED("common/rate-limit-exceeded", "ErrorMsg.RateLimitExceeded"),
    ;

    CommonError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private final String code;
    private final String message;

}