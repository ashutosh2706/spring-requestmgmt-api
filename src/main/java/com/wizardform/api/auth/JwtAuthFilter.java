package com.wizardform.api.auth;

import com.wizardform.api.exception.UnauthorizedServiceException;
import com.wizardform.api.service.JwtService;
import com.wizardform.api.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserService userService;
    private static final String SCHEME = "Bearer";
    private static final String JWT_TOKEN = "[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+\\.?[A-Za-z0-9-_.+/=]*";
    private static final String JWT_BEARER_TOKEN_PATTERN = SCHEME + " (" + JWT_TOKEN + ")";
    private static final Pattern JWT_TOKEN_PATTERN = Pattern.compile(JWT_BEARER_TOKEN_PATTERN, Pattern.CASE_INSENSITIVE);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String requestPath = request.getRequestURI().toString();
        final String source = request.getHeader("source");
        final String jwt, username;
        if(StringUtils.isEmpty(authHeader) || !org.apache.commons.lang3.StringUtils.startsWith(authHeader, "Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        // validate the token pattern and throw exception if pattern mismatches
        try {
            validateJwtTokenRegex(authHeader);
        } catch (UnauthorizedServiceException unauthorizedServiceException) {
            jwtService.sendErrorResponse(response, unauthorizedServiceException.getErrorMessage(),
                    unauthorizedServiceException.getErrorTrace() == null ? null : unauthorizedServiceException.getErrorTrace().toString(),
                    HttpStatus.UNAUTHORIZED);
            return;
        }

        jwt = authHeader.substring(7);
        // try to extract the username from token, throw exception if token is tampered
        try {
            username = jwtService.extractUserName(jwt);
        } catch (UnauthorizedServiceException ex) {
            jwtService.sendErrorResponse(response, ex.getErrorMessage(),
                    ex.getErrorTrace() == null ? null : ex.getErrorTrace().toString(),
                    HttpStatus.UNAUTHORIZED);
            return;
        }

        UserDetails userDetails = userService.userDetailsService().loadUserByUsername(username);
        boolean isTokenValidated = false;
        try {
            isTokenValidated = jwtService.isTokenValid(jwt, userDetails);
        } catch (UnauthorizedServiceException ex) {
            jwtService.sendErrorResponse(response, ex.getErrorMessage(),
                    ex.getErrorTrace() == null ? null : ex.getErrorTrace().toString(),
                    HttpStatus.UNAUTHORIZED);
            return;
        }

        if(!StringUtils.isEmpty(username) && SecurityContextHolder.getContext().getAuthentication() == null) {

            if(isTokenValidated) {
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                securityContext.setAuthentication(usernamePasswordAuthenticationToken);
                SecurityContextHolder.setContext(securityContext);
            }
        }

        filterChain.doFilter(request, response);
    }

    private void validateJwtTokenRegex(final String authHeader) throws UnauthorizedServiceException {
        Matcher tokenMatcher = JWT_TOKEN_PATTERN.matcher(authHeader);
        if(!tokenMatcher.matches()) {
            throw new UnauthorizedServiceException("Invalid Token");
        }
    }
}
