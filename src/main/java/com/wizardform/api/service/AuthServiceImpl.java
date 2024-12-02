package com.wizardform.api.service;

import com.wizardform.api.dto.AuthResponseDto;
import com.wizardform.api.dto.AuthRequestDto;
import com.wizardform.api.dto.RefreshTokenRequestDto;
import com.wizardform.api.exception.ExpiredRefreshTokenException;
import com.wizardform.api.exception.InvalidRefreshTokenException;
import com.wizardform.api.exception.UserNotFoundException;
import com.wizardform.api.model.RefreshToken;
import com.wizardform.api.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtService jwtService, RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public AuthResponseDto authenticateUser(AuthRequestDto authRequestDto) throws UserNotFoundException, BadCredentialsException {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDto.getEmail(), authRequestDto.getPassword()));
        if(authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtService.generateToken(userDetails);
            RefreshToken refreshToken = refreshTokenService.generateRefreshToken(authRequestDto.getEmail());
            AuthResponseDto authResponse = new AuthResponseDto(jwt, 300, "Bearer", refreshToken.getToken());
            return authResponse;
        }

        return null;
    }

    @Override
    public AuthResponseDto refreshToken(RefreshTokenRequestDto refreshTokenRequestDto) throws InvalidRefreshTokenException, ExpiredRefreshTokenException, UserNotFoundException {
        User user = refreshTokenService.getUserForRefreshToken(refreshTokenRequestDto.getToken());
        String jwt = jwtService.generateToken(user);
        return new AuthResponseDto(jwt, 300, "Bearer", refreshTokenRequestDto.getToken());
    }

}
