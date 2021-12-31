package io.coolexplorer.test.builder;

import io.coolexplorer.auth.enums.RoleType;
import io.coolexplorer.auth.model.Role;

public class TestRoleBuilder {
    public static Role userRole() {
        return new Role()
                .setId(1L)
                .setRoleName(RoleType.ROLE_USER.getName())
                .setDescription("User Role");
    }

    public static Role adminRole() {
        return new Role()
                .setId(1L)
                .setRoleName(RoleType.ROLE_ADMIN.getName())
                .setDescription("Admin Role");
    }
}
