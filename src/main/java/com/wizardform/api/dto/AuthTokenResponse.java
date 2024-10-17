package com.wizardform.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthTokenResponse {

    private String accessToken;
    private long expiresIn;
    private String tokenType;

    public AuthTokenResponse(String token, long expiresIn, String tokenType) {
        this.accessToken = token;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
    }
}
