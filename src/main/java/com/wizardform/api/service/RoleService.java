package com.wizardform.api.service;

import com.wizardform.api.dto.RoleDto;
import com.wizardform.api.exception.RoleNotFoundException;
import com.wizardform.api.model.Role;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoleService {
    List<RoleDto> getAllRole();
    Role getRoleByRoleId(int roleId) throws RoleNotFoundException;
    String getRoleType(int roleId) throws RoleNotFoundException;
    RoleDto addRole(RoleDto roleDTO);
    boolean updateRole(RoleDto roleDTO);
    void deleteRole(int roleId) throws RoleNotFoundException;
}
