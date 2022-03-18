package com.example.jsonexercise_2.model.dto.ex1;

import com.example.jsonexercise_2.model.entity.Car;
import com.example.jsonexercise_2.model.entity.Customer;
import com.google.gson.annotations.Expose;

import java.math.BigDecimal;

public class SalesDto {

    @Expose
    private BigDecimal discountPercentage;
    @Expose
    private Customer customer;
    @Expose
    private Car car;

    public SalesDto() {
    }

    public BigDecimal getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(BigDecimal discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }
}
