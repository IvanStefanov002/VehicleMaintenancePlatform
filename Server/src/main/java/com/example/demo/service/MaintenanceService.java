package com.example.demo.service;

import com.example.demo.dto.MaintenanceRequest;
import com.example.demo.dto.MaintenanceResponse;
import com.example.demo.dto.MaintenanceUpdateRequest;
import com.example.demo.model.MaintenanceRecord;
import java.util.List;

public interface MaintenanceService {
    /* create maintenance record */
    MaintenanceResponse create(String carId, MaintenanceRequest request);

    /* get by car id */
    List<MaintenanceResponse> getByCar(String carId);

    /* update */
    MaintenanceResponse update(String id, MaintenanceUpdateRequest request);

    /* delete */
    void delete(String id);
}
