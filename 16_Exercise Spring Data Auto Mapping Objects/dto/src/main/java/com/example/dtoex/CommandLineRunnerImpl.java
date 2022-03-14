package com.example.dtoex;

import com.example.dtoex.model.dto.GameAddDto;
import com.example.dtoex.model.dto.UserLoginDto;
import com.example.dtoex.model.dto.UserRegisterDto;
import com.example.dtoex.service.GameService;
import com.example.dtoex.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private final BufferedReader bufferedReader;
    private final UserService userService;
    private final GameService gameService;

    public CommandLineRunnerImpl(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
        this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run(String... args) throws Exception {

        while (true) {
            System.out.println("Please enter your command: ");
            String[] commands = bufferedReader.readLine().split("\\|");

            switch (commands[0]) {
                case "RegisterUser" -> userService
                        .registerUser(new UserRegisterDto(commands[1], commands[2], commands[3], commands[4]));

                case "LoginUser" -> userService
                        .loginUser(new UserLoginDto(commands[1], commands[2]));

                case "Logout" -> userService
                        .logout();

                case "AddGame" -> {
                    if (userService.userIsAdmin()) {
                        gameService
                                .addGame(new GameAddDto(commands[1], new BigDecimal(commands[2]),
                                        Double.parseDouble(commands[3]),
                                        commands[4], commands[5], commands[6], commands[7]));
                    } else {
                        System.out.println("You need to be admin!");
                    }
                }

                case "EditGame" -> {
                    if (userService.userIsAdmin()) {
                        gameService
                                .editGame(Long.parseLong(commands[1]), commands);
                    } else {
                        System.out.println("You need to be admin!");
                    }
                }

                case "DeleteGame" -> {
                    if (userService.userIsAdmin()) {
                        gameService
                                .deleteGame(Long.parseLong(commands[1]));
                    } else {
                        System.out.println("You need to be admin!");
                    }
                }

                case "AllGames" -> gameService
                        .printAllGames();

                case "DetailGame" -> gameService
                        .printDetailsOfGame(commands[1]);

                case "BuyGame" -> userService
                        .buyGame(commands[1]);

                case "OwnedGames" -> userService
                        .printOwnedGames();

                case "Exit" -> System.exit(0);

                default -> System.out.println("Unknown Command");
            }
        }
    }
}
