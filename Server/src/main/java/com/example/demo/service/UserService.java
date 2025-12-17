package com.example.demo.service;

import com.example.demo.dto.*;

import java.util.List;

public interface UserService {

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
