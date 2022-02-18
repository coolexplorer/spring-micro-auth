package io.coolexplorer.auth.service.impl;

import io.coolexplorer.auth.enums.RoleType;
import io.coolexplorer.auth.model.Role;
import io.coolexplorer.auth.repository.RoleRepository;
import io.coolexplorer.auth.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role getRole(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }

    @Override
    public Role getUserRole() {
        return roleRepository.findByRoleName(RoleType.ROLE_USER.name());
    }

    @Override
    public Role getBusinessRole() {
        return roleRepository.findByRoleName(RoleType.ROLE_BUSINESS.name());
    }

    @Override
    public Role getAdminRole() {
        return roleRepository.findByRoleName(RoleType.ROLE_ADMIN.name());
    }
}
