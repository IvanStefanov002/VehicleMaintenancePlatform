package com.example.demo.controller;

import com.example.demo.dto.MaintenanceRequest;
import com.example.demo.dto.MaintenanceResponse;
import com.example.demo.dto.MaintenanceUpdateRequest;
import com.example.demo.model.MaintenanceRecord;
import com.example.demo.service.MaintenanceService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/maintenance")
public class MaintenanceRecordController {

    private final MaintenanceService service;

    public MaintenanceRecordController(MaintenanceService service) {
        this.service = service;
    }

    @PostMapping("/{carId}")
    public MaintenanceResponse create(
            @PathVariable String carId,
            @Valid @RequestBody MaintenanceRequest request) {
        return service.create(carId, request);
    }

    @GetMapping("/{carId}")
    public List<MaintenanceResponse> getCarMaintenanceByCarId(@PathVariable String carId) {
        return service.getByCar(carId);
    }

    @PutMapping("/{id}")
    public MaintenanceResponse update(
            @PathVariable String id,
            @RequestBody MaintenanceUpdateRequest request
    ) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}
