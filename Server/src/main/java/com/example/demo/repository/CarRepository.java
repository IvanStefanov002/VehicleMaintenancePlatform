package com.example.demo.repository;

import com.example.demo.model.Car;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CarRepository extends MongoRepository<Car, String> {
    boolean existsByVin(String vin); //automatically implemented by Spring framework. -.-
}
