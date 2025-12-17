package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    /* if we have path /test?id=test&year=test
    * we can use @RequestParam  instead of
    * @PathVariable to get params
    *
    * example function:
    * @GetMapping
    * public List<MaintenanceResponse> getForCar(
    *   @RequestParam String id, // @RequestParam String id matches ?id=test
    *   @RequestParam(required = false) Integer year // @RequestParam Integer year matches &year=test
    *   ) {
    *       return service.getByCarAndYear(id, year);
    *   }
    * */

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = service.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add")
    public UserResponse create(@Valid @RequestBody UserRequest request) {
        return service.create(request);
    }

    @GetMapping
    public List<UserResponse> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable String id) {
        return service.findById(id);
    }

//    @PutMapping("/{id}")
//    public UserResponse update(
//            @PathVariable String id,
//            @RequestBody CarUpdateRequest request
//    ) {
//        return service.update(id, request);
//    }

    @DeleteMapping("/{id}")
    public MessageResponse delete(@PathVariable String id) {
        service.delete(id);
        return new MessageResponse("Car successfully deleted"); // set message as response
    }

}
