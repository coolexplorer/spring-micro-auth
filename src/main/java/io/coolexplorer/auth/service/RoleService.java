package io.coolexplorer.auth.service;

import io.coolexplorer.auth.model.Role;

public interface RoleService {
    Role getRole(String roleName);
    Role getUserRole();
    Role getBusinessRole();
    Role getAdminRole();
}
