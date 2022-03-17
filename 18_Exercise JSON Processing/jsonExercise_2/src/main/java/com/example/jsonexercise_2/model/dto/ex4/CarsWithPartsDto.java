package com.example.jsonexercise_2.model.dto.ex4;

import com.google.gson.annotations.Expose;

import java.util.List;

public class CarsWithPartsDto {

    @Expose
    private CarsDto car;
    @Expose
    private List<PartsDto> parts;

    public CarsWithPartsDto() {
    }

    public CarsDto getCars() {
        return car;
    }

    public void setCars(CarsDto cars) {
        this.car = cars;
    }

    public List<PartsDto> getParts() {
        return parts;
    }

    public void setParts(List<PartsDto> parts) {
        this.parts = parts;
    }
}
