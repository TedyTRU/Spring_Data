package com.example.dtoex.service.impl;

import com.example.dtoex.model.dto.UserLoginDto;
import com.example.dtoex.model.dto.UserRegisterDto;
import com.example.dtoex.model.entity.Game;
import com.example.dtoex.model.entity.User;
import com.example.dtoex.repository.GameRepository;
import com.example.dtoex.repository.UserRepository;
import com.example.dtoex.service.UserService;
import com.example.dtoex.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private User loggedInUser;
    private final GameRepository gameRepository;


    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, ValidationUtil validationUtil, GameRepository gameRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gameRepository = gameRepository;
    }

    @Override
    public void registerUser(UserRegisterDto userRegisterDto) {
        if (!userRegisterDto.getPassword().equals(userRegisterDto.getConfirmPassword())) {
            System.out.println("Password doesn't match");
            return;
        }

        Set<ConstraintViolation<UserRegisterDto>> violations =
                validationUtil.getViolation(userRegisterDto);

        if (!violations.isEmpty()) {
            violations
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);

            return;
        }

        //map dto to entity and save in DB

        User user = modelMapper.map(userRegisterDto, User.class);

        if (userRepository.findByEmail(user.getEmail()) != null) {
            System.out.println("User already registered");

            return;
        }

        userRepository.save(user);

        if (user.getId() == 1L) {
            user.setAdministrator(true);
        }

        userRepository.save(user);
        System.out.printf("%s was registered%n", user.getFullName());

    }

    @Override
    public void loginUser(UserLoginDto userLoginDto) {

        Set<ConstraintViolation<UserLoginDto>> violations =
                validationUtil.getViolation(userLoginDto);

        if (!violations.isEmpty()) {
            violations
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);

            return;
        }

        User user = userRepository
                .findByEmailAndPassword(userLoginDto.getEmail(), userLoginDto.getPassword())
                .orElse(null);

        if (user == null) {
            System.out.println("Incorrect username / password");

            return;
        }

        if (hasLoggedUser()) {
            System.out.println("There is already a logged-in user. Please first log out.");

            return;
        }

        loggedInUser = user;
        System.out.printf("Successfully logged in %s%n", user.getFullName());
    }

    @Override
    public void logout() {
        if (loggedInUser == null) {
            System.out.println("Cannot log out. No user was logged in.");

        } else {
            System.out.printf("User %s successfully logged out%n",
                    loggedInUser.getFullName());
            loggedInUser = null;

        }
    }

    @Override
    public boolean hasLoggedUser() {
        return loggedInUser != null;
    }

    @Override
    public void buyGame(String gameTitle) {
        Game game = gameRepository.findGameByTitle(gameTitle);

        if (loggedInUser == null) {
            System.out.println("No user is logged in.");
            return;
        }

        if (game == null) {
            System.out.println("No such game exist.");
            return;
        }

        Game existingGame = loggedInUser.getGames()
                .stream()
                .filter(game1 -> game1.getTitle().equals(gameTitle))
                .findAny()
                .orElse(null);

        if (existingGame != null) {
            System.out.printf("User %s already has the game %s%n", loggedInUser.getFullName(), gameTitle);
            return;
        }

        loggedInUser.getGames().add(game);
        userRepository.save(loggedInUser);
        System.out.printf("User %s has successfully bought the game %s%n",loggedInUser.getFullName(), game.getTitle());

    }

    @Override
    public void printOwnedGames() {
        if (loggedInUser == null) {
            System.out.println("No user is logged in.");
            return;
        }

        if (loggedInUser.getGames().size() == 0) {
            System.out.printf("User %s has no both games.%n", loggedInUser.getFullName());
            return;
        }

        loggedInUser
                .getGames()
                .forEach(game -> System.out.println(game.getTitle()));
    }
}
