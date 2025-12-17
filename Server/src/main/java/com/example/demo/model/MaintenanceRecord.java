package com.example.demo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Data
@Document(collection = "maintenance_records")
public class MaintenanceRecord {

    @Id
    private String id;
    private String carId;
    private LocalDate serviceDate;
    private String description;
    private int mileage;
    private double cost;
    private int nextServiceAt;
}
