package com.wizardform.api.service;

import com.wizardform.api.exception.UserNotFoundException;
import com.wizardform.api.model.User;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtServiceImpl implements JwtService {

    @Autowired
    private final UserService userService;

    public JwtServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String generateToken(UserDetails userDetails) throws UserNotFoundException {
        // Get existing user by username
        User user = userService.getUserByEmail(userDetails.getUsername());
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getUserId());
        claims.put("firstName", user.getFirstName());
        claims.put("lastName", user.getLastName());
        claims.put("roles", userDetails.getAuthorities());
        return Jwts.builder().setSubject(userDetails.getUsername())
                .addClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * 25)))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // extract claims from given jwt token
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parserBuilder().setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }

    private Key getSignKey() {
        Dotenv dotenv = Dotenv.load();
        byte[] key = Decoders.BASE64.decode(dotenv.get("SIGN_KEY"));
        return Keys.hmacShaKeyFor(key);
    }

    @Override
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUserName(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

}
