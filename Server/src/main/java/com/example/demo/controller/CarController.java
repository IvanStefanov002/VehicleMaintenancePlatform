package com.example.demo.controller;

import com.example.demo.auth.AuthHelper;
import com.example.demo.dto.CarRequest;
import com.example.demo.dto.CarResponse;
import com.example.demo.dto.CarUpdateRequest;
import com.example.demo.dto.MessageResponse;
import com.example.demo.service.CarService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/cars")
public class CarController {

    private final AuthHelper authHelper;
    private final CarService service;


    public CarController(CarService service, AuthHelper authHelper) {
        this.service = service;
        this.authHelper = authHelper;
    }

    @PostMapping
    public CarResponse create(
            @RequestHeader(value = "Authorization") String authHeader,
            @Valid @RequestBody CarRequest request
    ) {
        authHelper.requireAuth(authHeader); // check authorization
        return service.create(request);
    }

    @GetMapping
    public List<CarResponse> getAll( @RequestHeader( value = "Authorization") String authHeader ) {
        authHelper.requireAuth(authHeader);
        return service.findAll();
    }

    @GetMapping("/{id}")
    public CarResponse getById(@PathVariable String id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public CarResponse update(
            @PathVariable String id,
            @RequestBody CarUpdateRequest request
    ) {
        return service.update(id, request);
    }

    @PutMapping("/{id}/mileage")
    public CarResponse updateMileage(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable String id,
            @RequestBody CarUpdateRequest request
    ) {
        /* @RequestHeader(value = "Authorization", required = false) String authHeader
        * required = false -> allows custom response message.
        * without required -> throws 400 Bad Request instantly.
        */

        /* check authorization first */
        authHelper.requireAuth(authHeader);

        return service.update(id, request);
    }


    @DeleteMapping("/{id}")
    public MessageResponse delete(@PathVariable String id) {
        service.delete(id);
        return new MessageResponse("Car successfully deleted"); // set message as response
    }

}
