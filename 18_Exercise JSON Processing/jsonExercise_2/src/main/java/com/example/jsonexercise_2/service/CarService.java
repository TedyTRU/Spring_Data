package com.example.jsonexercise_2.service;

import com.example.jsonexercise_2.model.dto.ex2.CarsPrintDto;
import com.example.jsonexercise_2.model.dto.ex4.CarsWithPartsDto;
import com.example.jsonexercise_2.model.entity.Car;

import java.io.IOException;
import java.util.List;

public interface CarService {

    void seedCars() throws IOException;

    Car getRandomCar();

    List<CarsPrintDto> getCarByMake(String carMake);

    List<CarsWithPartsDto> getCarsWithParts();
}
