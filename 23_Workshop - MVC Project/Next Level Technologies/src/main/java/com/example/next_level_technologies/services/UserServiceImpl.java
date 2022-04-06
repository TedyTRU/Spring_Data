package com.example.next_level_technologies.services;

import com.example.next_level_technologies.entities.dto.UserRegisterDto;
import com.example.next_level_technologies.entities.models.User;
import com.example.next_level_technologies.repositories.UserRepository;
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

        return false;
    }
}
