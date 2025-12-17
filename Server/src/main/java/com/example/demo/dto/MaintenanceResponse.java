package com.example.demo.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MaintenanceResponse {
    private String id;
    private String carId;
    private LocalDate serviceDate;
    private String description;
    private int mileage;
    private double cost;
    private int nextServiceAt;
}
