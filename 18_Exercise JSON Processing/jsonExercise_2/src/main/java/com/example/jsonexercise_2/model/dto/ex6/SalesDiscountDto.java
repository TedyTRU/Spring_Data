package com.example.jsonexercise_2.model.dto.ex6;

import com.example.jsonexercise_2.model.dto.ex4.CarsDto;
import com.google.gson.annotations.Expose;

import java.math.BigDecimal;

public class SalesDiscountDto {

    @Expose
    private CarsDto car;
    @Expose
    private String customerName;
    @Expose
    private BigDecimal discountPercentage;
    @Expose
    private BigDecimal price;
    @Expose
    private BigDecimal priceWithDiscount;

    public SalesDiscountDto() {
    }

    public CarsDto getCar() {
        return car;
    }

    public void setCar(CarsDto car) {
        this.car = car;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public BigDecimal getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(BigDecimal discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPriceWithDiscount() {
        return priceWithDiscount;
    }

    public void setPriceWithDiscount(BigDecimal priceWithDiscount) {
        this.priceWithDiscount = priceWithDiscount;
    }

}
