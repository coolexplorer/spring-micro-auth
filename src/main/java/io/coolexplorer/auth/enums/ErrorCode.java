package io.coolexplorer.auth.enums;

import lombok.Getter;

@Getter
public enum ErrorCode {
    AUTH_USER_NOT_FOUND("error.auth.user.not.found");

    private String messageKey;

    ErrorCode(String messageKey) {
        this.messageKey = messageKey;
    }
}
