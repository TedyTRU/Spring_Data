package com.example.xmlexercise.service;

import com.example.xmlexercise.model.dto.ex2.UserViewRootDto;
import com.example.xmlexercise.model.dto.ex4.UsersRootDto;
import com.example.xmlexercise.model.dto.seed.UserSeedDto;
import com.example.xmlexercise.model.entity.User;

import java.util.List;

public interface UserService {

    long getEntityCount();

    void seedUsers(List<UserSeedDto> users);

    User getRandomUser();

    UserViewRootDto findUsersWithMoreThanOneSoldProducts();

    UsersRootDto findAllUsersWithProducts();

}
