package com.example.jsonexercise_2.service.Impl;

import com.example.jsonexercise_2.model.dto.ex2.CarsPrintDto;
import com.example.jsonexercise_2.model.dto.ex4.CarsDto;
import com.example.jsonexercise_2.model.dto.ex4.CarsWithPartsDto;
import com.example.jsonexercise_2.model.dto.ex4.PartsDto;
import com.example.jsonexercise_2.model.dto.seed.CarSeedDto;
import com.example.jsonexercise_2.model.entity.Car;
import com.example.jsonexercise_2.repository.CarRepository;
import com.example.jsonexercise_2.service.CarService;
import com.example.jsonexercise_2.service.PartService;
import com.example.jsonexercise_2.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static com.example.jsonexercise_2.constants.GlobalConstants.RESOURCE_FILE_PATH;

@Service
public class CarServiceImpl implements CarService {

    private static final String CARS_FILE_NAME = "cars.json";

    private final CarRepository carRepository;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final PartService partService;

    public CarServiceImpl(CarRepository carRepository, Gson gson, ValidationUtil validationUtil, ModelMapper modelMapper, PartService partService) {
        this.carRepository = carRepository;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.partService = partService;
    }

    @Override
    public void seedCars() throws IOException {

        if (carRepository.count() == 0) {

            String content = Files
                    .readString(Path.of(RESOURCE_FILE_PATH + CARS_FILE_NAME));

            CarSeedDto[] carSeedDto = gson.fromJson(content, CarSeedDto[].class);

            Arrays.stream(carSeedDto)
                    .filter(validationUtil::isValid)
                    .map(CarSeedDto -> {
                        Car car = modelMapper.map(CarSeedDto, Car.class);
                        car.setPartList(partService.getRandomPartList());
                        return car;
                    })
                    .forEach(carRepository::save);

        }
    }

    @Override
    public Car getRandomCar() {
        long randomId = ThreadLocalRandom.current().nextLong(1, carRepository.count() + 1);
        return carRepository.findById(randomId).orElse(null);
    }

    @Override
    public List<CarsPrintDto> getCarByMake(String carMake) {

        return carRepository
                .findAllByMakeOrderByModelThenByTravelledDistanceDesc("Toyota")
                .stream()
                .map(car -> modelMapper.map(car, CarsPrintDto.class))
                .toList();

    }

    @Override
    public List<CarsWithPartsDto> getCarsWithParts() {

        return carRepository.findAll().stream()
                .map(car -> {
                    CarsWithPartsDto carsWithPartsDto = new CarsWithPartsDto();

                    CarsDto carDto = modelMapper.map(car, CarsDto.class);
                    List<PartsDto> partsDtos = car.getPartList().stream().map(part -> modelMapper.map(part, PartsDto.class)).toList();

                    carsWithPartsDto.setCars(carDto);
                    carsWithPartsDto.setParts(partsDtos);

                    return carsWithPartsDto;

                }).toList();
    }


}
