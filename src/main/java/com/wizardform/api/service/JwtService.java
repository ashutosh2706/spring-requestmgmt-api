package com.wizardform.api.service;

import com.wizardform.api.exception.UnauthorizedServiceException;
import com.wizardform.api.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface JwtService {

    String extractUserName(String token) throws UnauthorizedServiceException;
    String generateToken(UserDetails userDetails) throws UserNotFoundException;
    void sendErrorResponse(HttpServletResponse response, String message, String details, HttpStatus httpStatus) throws IOException;
    boolean isTokenValid(String token, UserDetails userDetails) throws UnauthorizedServiceException;

}
