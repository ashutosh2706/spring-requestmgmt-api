package com.wizardform.api.controller;

import com.wizardform.api.dto.AuthRequestDto;
import com.wizardform.api.dto.AuthResponseDto;
import com.wizardform.api.dto.UserResponseDTO;
import com.wizardform.api.dto.UserDto;
import com.wizardform.api.dto.RefreshTokenRequestDto;
import com.wizardform.api.exception.ExpiredRefreshTokenException;
import com.wizardform.api.exception.InvalidRefreshTokenException;
import com.wizardform.api.exception.RoleNotFoundException;
import com.wizardform.api.exception.UserNotFoundException;
import com.wizardform.api.service.AuthService;
import com.wizardform.api.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@Tag(name = "Authentication Controller", description = "Handles auth functions like login, register and token refresh")
@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping(value = "login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> loginUser(@Valid @RequestBody AuthRequestDto authRequest)
            throws UserNotFoundException, BadCredentialsException {
        AuthResponseDto authResponse = authService.authenticateUser(authRequest);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping(value = "register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDto userDto)
            throws RoleNotFoundException {
        UserResponseDTO addedUser = userService.addUser(userDto);
        return ResponseEntity.created(URI.create("auth/login")).body(addedUser);
    }

    @PostMapping(value = "refreshToken", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequestDto tokenRequestDto)
            throws InvalidRefreshTokenException, ExpiredRefreshTokenException, UserNotFoundException {
        AuthResponseDto authResponse = authService.refreshToken(tokenRequestDto);
        return ResponseEntity.ok(authResponse);
    }

}
