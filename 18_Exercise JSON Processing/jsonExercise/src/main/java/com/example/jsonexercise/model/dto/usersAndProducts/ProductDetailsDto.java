package com.example.jsonexercise.model.dto.usersAndProducts;

import com.google.gson.annotations.Expose;

import java.math.BigDecimal;

public class ProductDetailsDto {

    @Expose
    private String name;
    @Expose
    private BigDecimal price;

    public ProductDetailsDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
