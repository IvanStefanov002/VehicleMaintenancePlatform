package com.example.demo.dto;

import lombok.Data;

@Data
public class CarResponse {
    private String id;
    private String owner;
    private String brand;
    private String model;
    private int year;
    private String vin;
    private String mileage;
}
