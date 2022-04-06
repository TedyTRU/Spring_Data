package com.example.next_level_technologies.services;

import com.example.next_level_technologies.entities.dto.UserRegisterDto;

public interface UserService {

    boolean register(UserRegisterDto user);

}
