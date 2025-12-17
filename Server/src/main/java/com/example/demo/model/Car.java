package com.example.demo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "cars")
public class Car {
    @Id
    private String id;
    private String owner;
    private String brand;
    private String model;
    private int year;
    private String vin;
    private String mileage;
}
