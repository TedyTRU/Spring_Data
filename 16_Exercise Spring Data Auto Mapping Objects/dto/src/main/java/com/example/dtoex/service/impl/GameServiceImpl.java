package com.example.dtoex.service.impl;

import com.example.dtoex.model.dto.GameAddDto;
import com.example.dtoex.model.entity.Game;
import com.example.dtoex.repository.GameRepository;
import com.example.dtoex.service.GameService;
import com.example.dtoex.service.UserService;
import com.example.dtoex.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import java.util.Set;

@Service
public class GameServiceImpl implements GameService {

    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final GameRepository gameRepository;
    private final UserService userService;

    public GameServiceImpl(ModelMapper modelMapper, ValidationUtil validationUtil, GameRepository gameRepository, UserService userService) {
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gameRepository = gameRepository;
        this.userService = userService;
    }


    @Override
    public void addGame(GameAddDto gameAddDto) {

        if (!userService.hasLoggedUser()) {
            System.out.println("Can not add game%nNo logged user found");
        }

        Set<ConstraintViolation<GameAddDto>> violations =
                validationUtil.getViolation(gameAddDto);

        if (!violations.isEmpty()) {
            violations
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);

            return;
        }

        Game game = modelMapper.map(gameAddDto, Game.class);

        //save in DB

    }
}
