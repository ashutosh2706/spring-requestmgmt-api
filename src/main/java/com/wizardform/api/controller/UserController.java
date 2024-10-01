package com.wizardform.api.controller;

import com.wizardform.api.dto.*;
import com.wizardform.api.exception.RoleNotFoundException;
import com.wizardform.api.exception.UserNotFoundException;
import com.wizardform.api.helper.QueryParams;
import com.wizardform.api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAllUsers(@Valid @ModelAttribute QueryParams queryParams) {
        PagedResponseDto<UserResponseDTO> response = userService.getAllUsers(queryParams.getSearchTerm(), queryParams.getPageNumber(), queryParams.getPageSize());
        return ResponseEntity.ok(response);
    }

    @PutMapping("allow/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> allowUser(@PathVariable long userId) throws UserNotFoundException {
        userService.allowUser(userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("ChangeRole")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> changeRole(@Valid ChangeRoleDto changeRoleDTO) throws RoleNotFoundException {
        userService.changeRole(changeRoleDTO.getUserId(), changeRoleDTO.getRoleId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("delete/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable long userId) throws UserNotFoundException {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

}
