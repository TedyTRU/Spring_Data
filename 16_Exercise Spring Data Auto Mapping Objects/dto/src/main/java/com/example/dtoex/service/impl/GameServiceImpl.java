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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
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
        game.setReleaseDate(LocalDate.parse(gameAddDto.getReleaseDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy")));

        //save in DB

        if (gameRepository.findGameByTitle(game.getTitle()) != null) {
            System.out.printf("Game %s already exist%n", game.getTitle());
            return;
        }

        gameRepository.save(game);

        System.out.printf("Added %s%n", game.getTitle());
    }

    @Override
    public void editGame(Long gameId, String[] commands) {

        Game game = gameRepository
                .findById(gameId)
                .orElse(null);

        if (game == null) {
            System.out.printf("Game with id %d does not exist%n", gameId);
            return;
        }

        editedGame(commands, game);

        GameAddDto gameAddDto = new GameAddDto(game.getTitle(), game.getPrice(), game.getSize(),
                game.getTrailer(), game.getImageThumbnail(), game.getDescription(), String.valueOf(game.getReleaseDate()));

        Set<ConstraintViolation<GameAddDto>> violations = validationUtil
                .getViolation(gameAddDto);
        if (!violations.isEmpty()) {
            violations
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);

            return;
        }

        gameRepository.save(game);

        System.out.printf("Edited %s%n", game.getTitle());
    }

    private void editedGame(String[] commands, Game game) {
        String[] tokens = Arrays.stream(commands).skip(1).toArray(String[]::new);

        for (String token : tokens) {
            String[] fields = token.split("=");
            switch (fields[0]) {
                case "title" -> game.setTitle(fields[1]);
                case "trailer" -> game.setTrailer(fields[1]);
                case "imageThumbnail" -> game.setImageThumbnail(fields[1]);
                case "size" -> game.setSize(Double.parseDouble(fields[1]));
                case "price" -> game.setPrice(new BigDecimal(fields[1]));
                case "description" -> game.setDescription(fields[1]);
                case "releaseDate" -> game
                        .setReleaseDate(LocalDate.parse(fields[1], DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            }
        }
    }

    @Override
    public void deleteGame(Long gameId) {
        Game game = gameRepository
                .findById(gameId)
                .orElse(null);

        if (game == null) {
            System.out.printf("Game with id %d does not exist%n", gameId);
            return;
        }

        gameRepository.delete(game);
        System.out.printf("Deleted %s%n", game.getTitle());
    }

    @Override
    public void printAllGames() {
        gameRepository
                .findAll().forEach(game -> System.out.printf("%s %.2f%n", game.getTitle(), game.getPrice()));
    }

    @Override
    public void printDetailsOfGame(String title) {
        Game game = gameRepository
                .findGameByTitle(title);

        System.out.printf("Title: %s%nPrice: %.2f%nDescription: %s%nRelease date: %s%n",
                game.getTitle(), game.getPrice(), game.getDescription(), game.getReleaseDate());
    }
}
