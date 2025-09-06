package com.wizardform.api.service;

import com.wizardform.api.dto.*;
import com.wizardform.api.exception.ExpiredRefreshTokenException;
import com.wizardform.api.exception.InvalidRefreshTokenException;
import com.wizardform.api.exception.UserNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    AuthResponseDto authenticateUser(AuthRequestDto authRequestDto) throws UserNotFoundException, BadCredentialsException;
    AuthResponseDto authenticateUser(String email, String password) throws UserNotFoundException, BadCredentialsException;
    AuthResponseDto refreshToken(RefreshTokenRequestDto refreshTokenRequestDto) throws InvalidRefreshTokenException, ExpiredRefreshTokenException, UserNotFoundException;
}
