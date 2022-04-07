package com.example.next_level_technologies.services.impl;

import com.example.next_level_technologies.entities.dto.UserLoginDto;
import com.example.next_level_technologies.entities.dto.UserRegisterDto;
import com.example.next_level_technologies.entities.models.User;
import com.example.next_level_technologies.repositories.UserRepository;
import com.example.next_level_technologies.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    public final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean register(UserRegisterDto userRequest) {

        if (this.userRepository.existsByUsernameOrEmail(
                userRequest.getUsername(),
                userRequest.getEmail()
        )) {
            return false;
        }

        if (!userRequest.getPassword().equals(userRequest.getConfirmPassword())) {
            return false;
        }

        User user = this.modelMapper.map(userRequest, User.class);

        this.userRepository.save(user);

        return true;
    }

    @Override
    public Long validateUserLoginDetails(UserLoginDto userRequest) {

        User user = this.userRepository.findFirstByUsername(userRequest.getUsername());

        if (user == null) {
            return null;
        }

        if (!user.getPassword().equals(userRequest.getPassword())) {
            return null;
        }

        return user.getId();
    }
}
