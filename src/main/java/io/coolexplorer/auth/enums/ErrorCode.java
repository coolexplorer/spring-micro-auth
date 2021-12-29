package io.coolexplorer.auth.enums;

import lombok.Getter;

@Getter
public enum ErrorCode {
    AUTH_USER_NOT_FOUND("error.auth.user.not.found"),
    JWT_TOKEN_EXPIRED("error.jwt.token.expired"),
    JWT_TOKEN_INVALID("error.jwt.token.invalid");

    private final String messageKey;

    ErrorCode(String messageKey) {
        this.messageKey = messageKey;
    }
}
