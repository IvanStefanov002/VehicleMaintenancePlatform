package com.example.demo.controller;

import com.example.demo.dto.CarRequest;
import com.example.demo.dto.CarResponse;
import com.example.demo.dto.CarUpdateRequest;
import com.example.demo.dto.MessageResponse;
import com.example.demo.service.CarService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/cars")
public class CarController {

    private final CarService service;

    public CarController(CarService service) {
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

    @PostMapping
    public CarResponse create(@Valid @RequestBody CarRequest request) {
        return service.create(request);
    }

    @GetMapping
    public List<CarResponse> getAll() {
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
            @PathVariable String id,
            @RequestBody CarUpdateRequest request
    ) {
        return service.update(id, request);
    }


    @DeleteMapping("/{id}")
    public MessageResponse delete(@PathVariable String id) {
        service.delete(id);
        return new MessageResponse("Car successfully deleted"); // set message as response
    }

}
