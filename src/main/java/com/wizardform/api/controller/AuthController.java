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
import com.wizardform.api.model.RefreshToken;
import com.wizardform.api.model.User;
import com.wizardform.api.service.JwtService;
import com.wizardform.api.service.RefreshTokenService;
import com.wizardform.api.service.UserService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    @Autowired
    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtService jwtService, RefreshTokenService refreshTokenService) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody AuthRequestDto authRequestDto) throws UserNotFoundException, BadCredentialsException {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDto.getEmail(), authRequestDto.getPassword()));
        if(authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtService.generateToken(userDetails);
            RefreshToken refreshToken = refreshTokenService.generateRefreshToken(authRequestDto.getEmail());
            AuthResponseDto authResponse = new AuthResponseDto(jwt, 300, "Bearer", refreshToken.getToken());
            return ResponseEntity.ok(authResponse);
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDto userDto) throws RoleNotFoundException {
        UserResponseDTO addedUser = userService.addUser(userDto);
        return ResponseEntity.created(URI.create("auth/login")).body(addedUser);
    }

    @PostMapping("refreshToken")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequestDto tokenRequestDto) throws InvalidRefreshTokenException, ExpiredRefreshTokenException, UserNotFoundException {
        User user = refreshTokenService.getUserForRefreshToken(tokenRequestDto.getToken());
        String jwt = jwtService.generateToken(user);
        AuthResponseDto authResponse = new AuthResponseDto(jwt, 300, "Bearer", tokenRequestDto.getToken());
        return ResponseEntity.ok(authResponse);
    }

}
