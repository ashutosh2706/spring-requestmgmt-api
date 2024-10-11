package com.wizardform.api.service;

import com.wizardform.api.dto.PagedResponseDto;
import com.wizardform.api.dto.UserDto;
import com.wizardform.api.dto.UserResponseDTO;
import com.wizardform.api.exception.RoleNotFoundException;
import com.wizardform.api.exception.UserNotFoundException;
import com.wizardform.api.helper.Utils;
import com.wizardform.api.mapper.UserMapper;
import com.wizardform.api.model.Role;
import com.wizardform.api.model.User;
import com.wizardform.api.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    @Override
    public PagedResponseDto<UserResponseDTO> getAllUsers(String searchTerm, int pageNumber, int pageSize, String sortField, String sortDirection) throws IllegalArgumentException {

        sortField = sortField.trim().isEmpty() ? "userId" : sortField;
        if(!isValidSortField(sortField)) {
            throw new IllegalArgumentException("Invalid sort field: " + sortField);
        }

        Sort sort = Sort.by(sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortField);
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize, sort);
        Page<User> userPage = userRepository.findAll(pageRequest);
        List<User> users = userPage.getContent();
        List<UserResponseDTO> result = new ArrayList<>();

        for(User user: users) {
            UserResponseDTO userResponseDTO = new UserResponseDTO();
            userResponseDTO.setUserId(user.getUserId());
            userResponseDTO.setFirstName(user.getFirstName());
            userResponseDTO.setLastName(user.getLastName());
            userResponseDTO.setEmail(user.getEmail());
            userResponseDTO.setAllowed(user.getIsActive());
            userResponseDTO.setRoleId(user.getRole().getRoleId());
            result.add(userResponseDTO);
        }

        return new PagedResponseDto<>(pageNumber, userPage.getNumberOfElements(), userPage.getTotalPages(), userPage.getTotalElements(), result);
    }

    /**
     * WARNING!!!
     * @return UserDTO when using in controller
     */
    @Override
    public User getUserByUserId(long userId) throws UserNotFoundException {
        Optional<User> optionalUser = userRepository.findByUserId(userId);
        return optionalUser.orElseThrow(() -> new UserNotFoundException("User with id (" + userId + ") was not found"));
    }

    @Override
    public void changeRole(long userId, int roleId) throws RoleNotFoundException{
        Optional<User> existingUser = userRepository.findByUserId(userId);
        Role role = roleService.getRoleByRoleId(roleId);
        if(existingUser.isPresent() && role != null) {
            User user = existingUser.get();
            user.setRole(role);
            userRepository.save(user);
        } else throw new EntityNotFoundException("Requested entity was not found.");
    }

    @Override
    public UserResponseDTO addUser(UserDto userDTO) throws RoleNotFoundException {
        Role existingRole = roleService.getRoleByRoleId(userDTO.getRoleId());
        if(existingRole != null) {
            User user = UserMapper.INSTANCE.userDTOToUser(userDTO);
            user.setPassword(Utils.generateHash(userDTO.getPassword()));
            user.setIsActive(false);
            user.setRole(existingRole);
            User savedUser = userRepository.save(user);
            return UserMapper.INSTANCE.userToUserResponseDTO(savedUser);
        } else throw new RoleNotFoundException("Role with id: " + userDTO.getRoleId() + " does not exist");
    }

    @Override
    @Transactional
    public void allowUser(long userId) throws UserNotFoundException {
        Optional<User> existingUser = userRepository.findByUserId(userId);
        if(existingUser.isPresent()) {
            User user = existingUser.get();
            user.setIsActive(!user.getIsActive());
            userRepository.save(user);
        } else throw new UserNotFoundException("User with id: " + userId + " was not found");
    }

    @Override
    @Transactional
    public void deleteUser(long userId) throws UserNotFoundException {
        Optional<User> existingUser = userRepository.findByUserId(userId);
        if(existingUser.isPresent()) {
            userRepository.delete(existingUser.get());
        } else throw new UserNotFoundException("User with id: " + userId + " was not found");
    }

    /**
     * WARNING!!!
     * @return UserResponseDTO when using in controller
     */
    /// email is unique, so it will return unique user
    /// two methods to get user from db, 1. by userId (accessible to admin only) 2. by email (accessible to end users)
    @Override
    public User getUserByEmail(String email) throws UserNotFoundException {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        return optionalUser.orElseThrow(() -> new UserNotFoundException("User with email (" + email + ") was not found"));
    }

    // Service for security config
    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                Optional<User> optionalUser = userRepository.findByEmail(username);
                return optionalUser.orElseThrow(() -> new UsernameNotFoundException("User with email (" + username + ") was not found"));
            }
        };
    }

    private static boolean isValidSortField(String sortField) {
        for(Field field: User.class.getDeclaredFields()) {
            if(field.getName().equals(sortField))
                return true;
        }
        return false;
    }
}
