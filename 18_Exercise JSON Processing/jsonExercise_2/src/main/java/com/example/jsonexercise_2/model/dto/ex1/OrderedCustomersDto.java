package com.example.jsonexercise_2.model.dto.ex1;

import com.google.gson.annotations.Expose;

import java.time.LocalDateTime;
import java.util.List;

public class OrderedCustomersDto {

    @Expose
    private Long id;
    @Expose
    private String name;
    @Expose
    private String birthDate;
    @Expose
    private Boolean isYoungDriver;
    @Expose
    private SalesDto sales;

    public OrderedCustomersDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public Boolean getYoungDriver() {
        return isYoungDriver;
    }

    public void setYoungDriver(Boolean youngDriver) {
        isYoungDriver = youngDriver;
    }

    public SalesDto getSales() {
        return sales;
    }

    public void setSales(SalesDto sales) {
        this.sales = sales;
    }
}
