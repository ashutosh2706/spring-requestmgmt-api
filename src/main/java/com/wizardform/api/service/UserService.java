package com.wizardform.api.service;

import com.wizardform.api.dto.PagedResponseDto;
import com.wizardform.api.dto.UserDto;
import com.wizardform.api.dto.UserResponseDTO;
import com.wizardform.api.exception.RoleNotFoundException;
import com.wizardform.api.exception.UserNotFoundException;
import com.wizardform.api.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    public PagedResponseDto<UserResponseDTO> getAllUsers(String searchTerm, int pageNumber, int pageSize, String sortField, String sortDirection) throws IllegalArgumentException;
    public User getUserByUserId(long userId) throws UserNotFoundException;
    public void changeRole(long userId, int roleId) throws RoleNotFoundException;
    public UserResponseDTO addUser(UserDto userDTO) throws RoleNotFoundException;
    public void allowUser(long userId) throws UserNotFoundException;
    public void deleteUser(long userId) throws UserNotFoundException;
    public User getUserByEmail(String email) throws UserNotFoundException;
    public UserDetailsService userDetailsService();
}
