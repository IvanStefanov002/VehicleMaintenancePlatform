package com.example.demo.service.impl;

import com.example.demo.dto.CarRequest;
import com.example.demo.dto.CarResponse;
import com.example.demo.dto.CarUpdateRequest;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Car;
import com.example.demo.repository.CarRepository;
import com.example.demo.service.CarService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarServiceImpl implements CarService {

    private final CarRepository repository;

    public CarServiceImpl(CarRepository repository) {
        this.repository = repository;
    }

    /* create a car record */
    @Override
    public CarResponse create(CarRequest request) {

        /* check if car already exists - by VIN */
        if (repository.existsByVin(request.getVin())) {
            throw new NotFoundException("Car with this VIN already exists");
        }

        Car car = new Car();
        car.setOwner(request.getOwner());
        car.setBrand(request.getBrand());
        car.setModel(request.getModel());
        car.setYear(request.getYear());
        car.setVin(request.getVin());
        car.setMileage(request.getMileage());

        Car saved = repository.save(car);

        return toResponse(saved);
    }

    /* list of all cars */
    @Override
    public List<CarResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /* find car by id */
    @Override
    public CarResponse findById(String id) {
        Car car = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Car not found"));

        return toResponse(car);
    }

    /* update car data */
    @Override
    public CarResponse update(String id, CarUpdateRequest request) {
        Car car = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Car not found"));

        if (request.getBrand() != null) car.setBrand(request.getBrand());
        if (request.getModel() != null) car.setModel(request.getModel());
        if (request.getYear() != null) car.setYear(request.getYear());
        if (request.getVin() != null) car.setVin(request.getVin());
        if (request.getMileage() != null) car.setMileage(request.getMileage());

        Car saved = repository.save(car);
        return toResponse(saved);
    }

    /* delete a car */
    @Override
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException(String.format("Car with id[%s] not found", id));
        }
        repository.deleteById(id);
    }

    private CarResponse toResponse(Car car) {
        CarResponse response = new CarResponse();
        response.setId(car.getId());
        response.setOwner(car.getOwner());
        response.setBrand(car.getBrand());
        response.setModel(car.getModel());
        response.setYear(car.getYear());
        response.setVin(car.getVin());
        response.setMileage(car.getMileage());
        return response;
    }
}
