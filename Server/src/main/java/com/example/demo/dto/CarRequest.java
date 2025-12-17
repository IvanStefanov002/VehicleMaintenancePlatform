package com.example.demo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CarRequest {

    @NotBlank(message = "Owner is required")
    private String owner;

    @NotBlank(message = "Brand is required")
    private String brand;

    @NotBlank(message = "Model is required")
    private String model;

    @Min(value = 1900, message = "Year must be >= 1900")
    private int year;

    @NotBlank(message = "VIN is required")
    private String vin;

    @NotBlank(message = "Mileage is required")
    private String mileage;
}
