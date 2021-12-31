package io.coolexplorer.auth.enums;

import lombok.Getter;

public enum RoleType {
    ROLE_USER("ROLE_USER"),
    ROLE_BUSINESS("ROLE_BUSINESS"),
    ROLE_ADMIN("ROLE_ADMIN");

    @Getter
    private String name;

    RoleType(String name) {
        this.name = name;
    }
}
