package com.example.demo.dto;

import lombok.Data;

@Data
public class CarUpdateRequest {
    private String brand;
    private String model;
    private Integer year;
    private String vin;
    private String mileage;
}
