package com.example.demo.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private String message;
    private String userId;
    private String username;
    private String token;

    public LoginResponse(String message, String userId, String username, String token) {
        this.message = message;
        this.userId = userId;
        this.username = username;
        this.token = token;
    }
}