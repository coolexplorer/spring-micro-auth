package io.coolexplorer.auth.enums;

import lombok.Getter;

@Getter
public enum ExceptionCode {
    USERNAME_NOT_FOUND("exception.username.not.found"),
    BAD_CREDENTIALS("exception.bad.credential");

    private final String messageKey;

    ExceptionCode(String messageKey) {
        this.messageKey = messageKey;
    }
}
