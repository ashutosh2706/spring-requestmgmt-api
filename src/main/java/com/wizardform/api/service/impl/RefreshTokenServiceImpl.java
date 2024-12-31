package com.wizardform.api.service.impl;

import com.wizardform.api.exception.ExpiredRefreshTokenException;
import com.wizardform.api.exception.InvalidRefreshTokenException;
import com.wizardform.api.exception.UserNotFoundException;
import com.wizardform.api.model.RefreshToken;
import com.wizardform.api.model.User;
import com.wizardform.api.repository.RefreshTokenRepository;
import com.wizardform.api.service.RefreshTokenService;
import com.wizardform.api.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;
    @Value("${app.refresh-token.expiration}")
    private long refreshTokenExpiration;

    @Autowired
    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository, UserService userService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userService = userService;
    }

    @Override
    public RefreshToken generateRefreshToken(String username) throws UserNotFoundException {
        User user = userService.getUserByEmail(username);
        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expirationInstant(Instant.now().plusMillis(refreshTokenExpiration * 1000))
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken getRefreshTokenDetails(String token) throws InvalidRefreshTokenException, ExpiredRefreshTokenException {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByToken(token);
        if(refreshToken.isPresent()) {
            if(isRefreshTokenExpired(refreshToken.get())) {
                // delete the token from db if expired
                refreshTokenRepository.delete(refreshToken.get());
                log.error("ExpiredRefreshTokenException: Token expired {}", refreshToken.get());
                throw new ExpiredRefreshTokenException("Refresh token expired. Please authenticate to generate new token");
            }

            return rotateToken(refreshToken.get());

        } else {
            log.error("InvalidRefreshTokenException: Token invalid {}", refreshToken.get());
            throw new InvalidRefreshTokenException("Refresh token is invalid. Please provide valid refresh token");
        }
    }

    private boolean isRefreshTokenExpired(RefreshToken refreshToken) {
        return refreshToken.getExpirationInstant().compareTo(Instant.now()) < 0;
    }

    private RefreshToken rotateToken(RefreshToken existingToken) {
        existingToken.setToken(UUID.randomUUID().toString());
        existingToken.setExpirationInstant(Instant.now().plusMillis(refreshTokenExpiration * 1000));
        return refreshTokenRepository.save(existingToken);
    }
}
