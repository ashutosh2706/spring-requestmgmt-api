package com.wizardform.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wizardform.api.exception.ErrorResponse;
import com.wizardform.api.exception.UnauthorizedServiceException;
import com.wizardform.api.exception.UserNotFoundException;
import com.wizardform.api.model.User;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtServiceImpl implements JwtService {
    private final UserService userService;
    @Value("${app.jwt.expiration}")
    private long jwtExpiration;

    private static final String ENV_SIGN_KEY = "SIGN_KEY";
    private static final String ENV_ISSUER = "ISSUER";

    @Autowired
    public JwtServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String generateToken(UserDetails userDetails) throws UserNotFoundException {
        System.out.println("Jwt Expiration: " + jwtExpiration);
        // Get existing user by username
        User user = userService.getUserByEmail(userDetails.getUsername());
        Map<String, Object> claims = new HashMap<>();
//        claims.put("userId", user.getUserId());
//        claims.put("firstName", user.getFirstName());
//        claims.put("lastName", user.getLastName());
//        claims.put("email", user.getEmail());
        claims.put("roles", userDetails.getAuthorities());
        return Jwts.builder()
                .setHeaderParam("typ", "Bearer")
                .setSubject(userDetails.getUsername())
                .addClaims(claims)
                .setIssuer(getIssuer())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (jwtExpiration * 1000)))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // extract claims from given jwt token
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) throws UnauthorizedServiceException {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) throws UnauthorizedServiceException {
        try {

            return Jwts.parserBuilder().setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

        } catch (UnsupportedJwtException e) {
            throw new UnauthorizedServiceException("UnsupportedJwtException", e.getMessage(), e);
        } catch (MalformedJwtException e) {
            throw new UnauthorizedServiceException("MalformedJwtException", e.getMessage(), e);
        } catch (SignatureException e) {
            throw new UnauthorizedServiceException("SignatureException", e.getMessage(), e);
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedServiceException("ExpiredJwtException", e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            throw new UnauthorizedServiceException("IllegalArgumentException", e.getMessage(), e);
        }
    }

    private Key getSignKey() {
        Dotenv dotenv = Dotenv.load();
        byte[] key = Decoders.BASE64.decode(dotenv.get(ENV_SIGN_KEY));
        return Keys.hmacShaKeyFor(key);
    }

    private String getIssuer() {
        Dotenv dotenv = Dotenv.load();
        return dotenv.get(ENV_ISSUER);
    }

    @Override
    public String extractUserName(String token) throws UnauthorizedServiceException {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) throws UnauthorizedServiceException {
        final String username = extractUserName(token);
        final String issuer = extractIssuer(token);
        return username.equals(userDetails.getUsername()) && (getIssuer().equals(issuer)) && !isTokenExpired(token);
    }

    @Override
    public void sendErrorResponse(HttpServletResponse response, String message, String details, HttpStatus httpStatus) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse(message, details, httpStatus);
        response.setStatus(httpStatus.value());
        response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    }

    private boolean isTokenExpired(String token) throws UnauthorizedServiceException {
        return getTokenExpiration(token).before(new Date());
    }

    private Date getTokenExpiration(String token) throws UnauthorizedServiceException {
        return extractClaim(token, Claims::getExpiration);
    }

    private String extractIssuer(String token) throws UnauthorizedServiceException {
        return extractClaim(token, Claims::getIssuer);
    }

}
