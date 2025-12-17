package com.example.demo.repository;

import com.example.demo.model.MaintenanceRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface MaintenanceRecordRepository extends MongoRepository<MaintenanceRecord, String> {

    List<MaintenanceRecord> findByCarId(String carId);
}
