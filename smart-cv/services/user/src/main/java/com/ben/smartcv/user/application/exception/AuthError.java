package com.ben.smartcv.user.application.exception;

import lombok.Getter;

@Getter
public enum AuthError {
    EMAIL_ALREADY_IN_USE("auth/email-already-in-use", "ErrorMsg.EmailAlreadyInUse"),
    PASSWORD_MIS_MATCH("auth/password-mismatch", "ErrorMsg.PasswordMismatch"),
    TERMS_NOT_ACCEPTED("auth/terms-not-accepted", "ErrorMsg.TermsNotAccepted"),
    WRONG_PASSWORD("auth/wrong-password", "ErrorMsg.WrongPassword"),
    ACTIVATION_CODE_INVALID("auth/invalid-activation-code", "ErrorMsg.InvalidActivationCode"),
    CODE_INVALID("auth/code-invalid", "ErrorMsg.CodeInvalid"),
    ;

    AuthError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private final String code;
    private final String message;

}