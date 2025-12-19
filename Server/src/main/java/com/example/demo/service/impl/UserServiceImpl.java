package com.example.demo.service.impl;

import com.example.demo.dto.*;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Car;
import com.example.demo.model.User;
import com.example.demo.repository.CarRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.CarService;
import com.example.demo.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, UserRepository repository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    /* login */
    @Override
    public LoginResponse login(LoginRequest request) {

        User user = repository
                .findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return new LoginResponse(
                "Login successful",
                user.getId(),
                user.getUsername()
        );
    }

    /* create user record */
    @Override
    public UserResponse create(UserRequest request) {
        if (repository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        String rawPassword = request.getPassword();
        String hashPassword = passwordEncoder.encode(rawPassword);

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(hashPassword);

        User saved = repository.save(user);
        return toResponse(saved);
    }

    /* list of all cars */
    @Override
    public List<UserResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /* find car by id */
    @Override
    public UserResponse findById(String id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        return toResponse(user);
    }

    /* update car data */
//    @Override
//    public UserResponse update(String id, CarUpdateRequest request) {
//        Car car = repository.findById(id)
//                .orElseThrow(() -> new NotFoundException("Car not found"));
//
//        if (request.getBrand() != null) car.setBrand(request.getBrand());
//        if (request.getModel() != null) car.setModel(request.getModel());
//        if (request.getYear() != null) car.setYear(request.getYear());
//        if (request.getVin() != null) car.setVin(request.getVin());
//
//        Car saved = repository.save(car);
//        return toResponse(saved);
//    }

    /* delete a car */
    @Override
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException(String.format("User with id[%s] not found", id));
        }
        repository.deleteById(id);
    }

    private UserResponse toResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setPassword(user.getPassword());
        return response;
    }
}
