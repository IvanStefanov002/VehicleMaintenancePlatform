package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class MaintenanceUpdateRequest {
    private LocalDate serviceDate;
    private String description;
    private Integer mileage;
    private Double cost;
    private Integer nextServiceAt;
}
