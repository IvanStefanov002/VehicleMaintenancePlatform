package com.example.demo.service.impl;

import com.example.demo.dto.MaintenanceRequest;
import com.example.demo.dto.MaintenanceResponse;
import com.example.demo.dto.MaintenanceUpdateRequest;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.MaintenanceRecord;
import com.example.demo.repository.MaintenanceRecordRepository;
import com.example.demo.service.MaintenanceService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaintenanceServiceImpl implements MaintenanceService {

    private final MaintenanceRecordRepository repository;

    public MaintenanceServiceImpl(MaintenanceRecordRepository repository) {
        this.repository = repository;
    }

    @Override
    public MaintenanceResponse create(String carId, MaintenanceRequest request) {
        MaintenanceRecord record = new MaintenanceRecord();
        record.setCarId(carId);
        record.setServiceDate(request.getServiceDate());
        record.setDescription(request.getDescription());
        record.setMileage(request.getMileage());
        record.setCost(request.getCost());
        record.setNextServiceAt(request.getNextServiceAt());

        MaintenanceRecord saved = repository.save(record);

        return toResponse(saved);
    }

    @Override
    public List<MaintenanceResponse> getByCar(String carId) {
        return repository.findByCarId(carId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public MaintenanceResponse update(String id, MaintenanceUpdateRequest request) {
        MaintenanceRecord record = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Record not found"));

        if (request.getServiceDate() != null) record.setServiceDate(request.getServiceDate());
        if (request.getDescription() != null) record.setDescription(request.getDescription());
        if (request.getMileage() != null) record.setMileage(request.getMileage());
        if (request.getCost() != null) record.setCost(request.getCost());
        if (request.getNextServiceAt() != null) record.setNextServiceAt(request.getNextServiceAt());

        return toResponse(repository.save(record));
    }

    @Override
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Record not found");
        }
        repository.deleteById(id);
    }

    private MaintenanceResponse toResponse(MaintenanceRecord record) {
        MaintenanceResponse response = new MaintenanceResponse();
        response.setId(record.getId());
        response.setCarId(record.getCarId());
        response.setServiceDate(record.getServiceDate());
        response.setDescription(record.getDescription());
        response.setMileage(record.getMileage());
        response.setCost(record.getCost());
        response.setNextServiceAt(record.getNextServiceAt());
        return response;
    }
}
