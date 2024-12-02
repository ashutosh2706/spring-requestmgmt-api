package com.wizardform.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthResponseDto {

    private String access_token;
    private long expires_in;
    private String token_type;
    private String refresh_token;

    public AuthResponseDto(String token, long expiresIn, String tokenType, String refreshToken) {
        this.access_token = token;
        this.expires_in = expiresIn;
        this.token_type = tokenType;
        this.refresh_token = refreshToken;
    }
}
