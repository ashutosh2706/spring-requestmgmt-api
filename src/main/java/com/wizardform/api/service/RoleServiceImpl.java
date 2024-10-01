package com.wizardform.api.service;

import com.wizardform.api.dto.RoleDto;
import com.wizardform.api.exception.RoleNotFoundException;
import com.wizardform.api.mapper.RoleMapper;
import com.wizardform.api.model.Role;
import com.wizardform.api.repository.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<RoleDto> getAllRole() {
        List<Role> roles = roleRepository.findAll();
        return RoleMapper.INSTANCE.rolesToRoleDTOs(roles);
    }

    @Override
    public Role getRoleByRoleId(int roleId) throws RoleNotFoundException {
        Optional<Role> role = roleRepository.findByRoleId(roleId);
        if(role.isPresent()) {
            return role.get();
        } else throw new RoleNotFoundException("Role with id: " + roleId + " was not found");
    }

    @Override
    public String getRoleType(int roleId) throws RoleNotFoundException {
        Optional<Role> role = roleRepository.findByRoleId(roleId);
        if(role.isPresent()) {
            return role.get().getRoleType();
        } else throw new RoleNotFoundException("Role with id: " + roleId + " was not found");

    }

    @Override
    public RoleDto addRole(RoleDto roleDto) {
        Role role = RoleMapper.INSTANCE.roleDTOtoRole(roleDto);
        Role addedRole = roleRepository.save(role);
        return RoleMapper.INSTANCE.roleToRoleDTO(addedRole);
    }

    @Override
    @Transactional
    public boolean updateRole(RoleDto roleDto) {
        Role providedRole = RoleMapper.INSTANCE.roleDTOtoRole(roleDto);
        Optional<Role> existingRoleOptional = roleRepository.findByRoleId(providedRole.getRoleId());
        if(existingRoleOptional.isPresent()) {
            Role existingRole = existingRoleOptional.get();
            // roleId will remain same
            existingRole.setRoleType(providedRole.getRoleType());
            roleRepository.save(existingRole);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public void deleteRole(int roleId) throws RoleNotFoundException {
        Optional<Role> existingRole = roleRepository.findByRoleId(roleId);
        if(existingRole.isPresent()) {
            roleRepository.delete(existingRole.get());
        } else throw new RoleNotFoundException("Role with id: " + roleId + " was not found");
    }
}
