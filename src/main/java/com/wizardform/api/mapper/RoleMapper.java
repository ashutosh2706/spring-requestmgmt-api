package com.wizardform.api.mapper;

import com.wizardform.api.dto.RoleDto;
import com.wizardform.api.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface RoleMapper {
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);
    List<RoleDto> rolesToRoleDTOs(List<Role> roles);
    RoleDto roleToRoleDTO(Role role);
    Role roleDTOtoRole(RoleDto roleDTO);
}
