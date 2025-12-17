package com.example.demo.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private String message;
    private String userId;
    private String username;

    public LoginResponse(String message, String userId, String username) {
        this.message = message;
        this.userId = userId;
        this.username = username;
    }

    // getters
}