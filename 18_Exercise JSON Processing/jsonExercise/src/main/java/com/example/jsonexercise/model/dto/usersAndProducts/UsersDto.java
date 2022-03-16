package com.example.jsonexercise.model.dto.usersAndProducts;

import com.google.gson.annotations.Expose;

public class UsersDto {

    @Expose
    private String firstName;
    @Expose
    private String lastName;
    @Expose
    private Integer age;
    @Expose
    private CountOfSoldProductsDto soldProducts;

    public UsersDto() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public CountOfSoldProductsDto getSoldProducts() {
        return soldProducts;
    }

    public void setSoldProducts(CountOfSoldProductsDto soldProducts) {
        this.soldProducts = soldProducts;
    }
}
