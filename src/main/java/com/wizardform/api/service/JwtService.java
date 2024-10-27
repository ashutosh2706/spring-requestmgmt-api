package com.wizardform.api.service;

import com.wizardform.api.exception.UserNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public interface JwtService {

    String extractUserName(String token);
    String generateToken(UserDetails userDetails) throws UserNotFoundException;
    boolean isTokenValid(String token, UserDetails userDetails);

}
