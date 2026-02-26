package com.example.demo.auth;

import com.example.demo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class AuthHelper {

    private final UserService userService;

    public AuthHelper(UserService userService) {
        this.userService = userService;
    }

    public void requireAuth(String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Missing token"
            );
        }

        String token = authHeader.substring( "Bearer ".length() ).trim();

        if ( !userService.isTokenValid(token) ) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid token"
            );
        }
    }
}

