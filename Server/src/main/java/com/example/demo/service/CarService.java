package com.example.demo.service;

import com.example.demo.dto.CarRequest;
import com.example.demo.dto.CarResponse;
import com.example.demo.dto.CarUpdateRequest;

import java.util.List;

public interface CarService {

    /* create */
    CarResponse create(CarRequest request);

    /* search */
    List<CarResponse> findAll();
    CarResponse findById(String id);

    /* update */
    CarResponse update(String id, CarUpdateRequest request);
    void delete(String id);

}
