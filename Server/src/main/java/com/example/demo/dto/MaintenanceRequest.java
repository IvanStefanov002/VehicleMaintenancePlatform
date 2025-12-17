package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class MaintenanceRequest {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "Service date is required")
    private LocalDate serviceDate;

    @NotBlank(message = "Description is required")
    private String description;

    @Min(value = 1, message = "Mileage must be positive")
    private int mileage;

    @Min(value = 0, message = "Cost must be >= 0")
    private double cost;

    @Min(value = 0, message = "Next Mileage must be positive or 0")
    private int nextServiceAt;
}

