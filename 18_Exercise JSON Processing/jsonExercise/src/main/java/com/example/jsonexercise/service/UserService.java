package com.example.jsonexercise.service;

import com.example.jsonexercise.model.dto.successfullySoldProducts.UserSoldDto;
import com.example.jsonexercise.model.dto.usersAndProducts.CountOfSellersDto;
import com.example.jsonexercise.model.entity.User;

import java.io.IOException;
import java.util.List;

public interface UserService {

    void seedUsers() throws IOException;

    User findRandomUser();

    List<UserSoldDto> findAllUsersWithMoreThanOneSoldProducts();

    CountOfSellersDto findAllUsersWithSoldProducts();
}
