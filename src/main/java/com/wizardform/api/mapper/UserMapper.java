package com.wizardform.api.mapper;

import com.wizardform.api.dto.UserDto;
import com.wizardform.api.dto.UserResponseDTO;
import com.wizardform.api.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    User userDTOToUser(UserDto userDTO);
    @Mapping(source = "role.roleId", target = "roleId")
    UserResponseDTO userToUserResponseDTO(User user);
    @Mapping(source = "role.roleId", target = "roleId")
    @Mapping(target = "password", ignore = true)
    UserDto userToUserDTO(User user);
}
