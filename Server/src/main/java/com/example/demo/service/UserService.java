package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.model.User;

import java.util.List;

public interface UserService {

    /* authenticate */
    boolean isTokenValid(String rawToken);

    /* login */
    LoginResponse login(LoginRequest request);

    /* create */
    UserResponse create(UserRequest request);

    /* search */
    List<UserResponse> findAll();
    UserResponse findById(String id);

    /* update */
    //CarResponse update(String id, CarUpdateRequest request);
    void delete(String id);

}
