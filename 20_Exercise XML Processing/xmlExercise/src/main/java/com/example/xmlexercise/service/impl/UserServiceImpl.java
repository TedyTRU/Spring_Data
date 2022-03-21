package com.example.xmlexercise.service.impl;


import com.example.xmlexercise.model.dto.ex2.UserViewRootDto;
import com.example.xmlexercise.model.dto.ex2.UserWithProductsDto;
import com.example.xmlexercise.model.dto.ex4.ProductsDto;
import com.example.xmlexercise.model.dto.ex4.SoldProductsDto;
import com.example.xmlexercise.model.dto.ex4.UsersDto;
import com.example.xmlexercise.model.dto.ex4.UsersRootDto;
import com.example.xmlexercise.model.dto.seed.UserSeedDto;
import com.example.xmlexercise.model.entity.User;
import com.example.xmlexercise.repository.UserRepository;
import com.example.xmlexercise.service.UserService;
import com.example.xmlexercise.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final UserRepository userRepository;

    public UserServiceImpl(ModelMapper modelMapper, ValidationUtil validationUtil, UserRepository userRepository) {
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.userRepository = userRepository;
    }

    @Override
    public long getEntityCount() {
        return userRepository.count();
    }

    @Override
    public void seedUsers(List<UserSeedDto> users) {

        users.stream()
                .filter(validationUtil::isValid)
                .map(userSeedDto -> modelMapper.map(userSeedDto, User.class))
                .forEach(userRepository::save);

    }

    @Override
    public User getRandomUser() {
        long randomId = ThreadLocalRandom.current()
                .nextLong(1, userRepository.count() + 1);

        return userRepository.findById(randomId).orElse(null);
    }

    @Override
    public UserViewRootDto findUsersWithMoreThanOneSoldProducts() {
        UserViewRootDto userViewRootDto = new UserViewRootDto();

        userViewRootDto.setProducts(userRepository
                .findAllUsersWithMoreThanOneSoldProduct()
                .stream().map(user -> modelMapper.map(user, UserWithProductsDto.class))
                .collect(Collectors.toList()));

        return userViewRootDto;
    }

    @Override
    public UsersRootDto findAllUsersWithProducts() {
        List<User> users = userRepository.findAllUsersWithMoreThanOneSoldProductOrderedBySoldProducts();

        UsersRootDto usersRootDto = new UsersRootDto();

        List<UsersDto> usersDto = users.stream()
                .map(user -> {

                    UsersDto usersDto2 = modelMapper.map(user, UsersDto.class);
                    SoldProductsDto productsDto3 = new SoldProductsDto();

                    List<ProductsDto> productsDtos4 = user.getSoldProducts().stream()
                            .map(product -> modelMapper.map(product, ProductsDto.class)).toList();

                    productsDto3.setCount(user.getSoldProducts().size());
                    productsDto3.setProducts(productsDtos4);

                    usersDto2.setSoldProducts(productsDto3);

                    return usersDto2;
                }).toList();

        usersRootDto.setCount(usersDto.size());
        usersRootDto.setUsers(usersDto);

        return usersRootDto;
    }
}
