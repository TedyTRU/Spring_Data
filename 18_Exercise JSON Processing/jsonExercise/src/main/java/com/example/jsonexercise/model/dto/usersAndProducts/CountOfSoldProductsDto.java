package com.example.jsonexercise.model.dto.usersAndProducts;

import com.google.gson.annotations.Expose;

import java.util.List;

public class CountOfSoldProductsDto {

    @Expose
    private Integer count;
    @Expose
    private List<ProductDetailsDto> soldProducts;

    public CountOfSoldProductsDto() {
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<ProductDetailsDto> getSoldProducts() {
        return soldProducts;
    }

    public void setSoldProducts(List<ProductDetailsDto> soldProducts) {
        this.soldProducts = soldProducts;
    }
}
