package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.domain.dto.PlayerSeedDto;
import softuni.exam.domain.entities.Picture;
import softuni.exam.domain.entities.Player;
import softuni.exam.domain.entities.Team;
import softuni.exam.repository.PlayerRepository;
import softuni.exam.service.PictureService;
import softuni.exam.service.PlayerService;
import softuni.exam.service.TeamService;
import softuni.exam.util.ValidatorUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService {

    public static final String PLAYERS_FILE_PATH = "src/main/resources/files/json/players.json";

    private final PlayerRepository playerRepository;
    private final PictureService pictureService;
    private final TeamService teamService;
    private final ModelMapper modelMapper;
    private final ValidatorUtil validationUtil;
    private final Gson gson;

    public PlayerServiceImpl(PlayerRepository playerRepository, PictureService pictureService, TeamService teamService, ModelMapper modelMapper, ValidatorUtil validationUtil, Gson gson) {
        this.playerRepository = playerRepository;
        this.pictureService = pictureService;
        this.teamService = teamService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public String importPlayers() throws IOException {
        StringBuilder sb = new StringBuilder();

        PlayerSeedDto[] playerSeedDtos = gson.fromJson(readPlayersJsonFile(), PlayerSeedDto[].class);

        Arrays.stream(playerSeedDtos)
                .filter(playerSeedDto -> {
                    boolean isValid = validationUtil.isValid(playerSeedDto)
                            && teamService.isEntityExist(playerSeedDto.getTeam().getName())
                            && pictureService.isEntityExist(playerSeedDto.getPicture().getUrl());

                    sb
                            .append(isValid
                                    ? String.format("Successfully imported player: %s %s", playerSeedDto.getFirstName(), playerSeedDto.getLastName())
                                    : "Invalid player")
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(playerSeedDto -> {
                    Player player = modelMapper.map(playerSeedDto, Player.class);
                    Picture picture = pictureService.findByUrl(playerSeedDto.getPicture().getUrl());
                    Team team = teamService.findByName(playerSeedDto.getTeam().getName());

                    player.setPicture(picture);
                    player.setTeam(team);

                    return player;
                })
                .forEach(playerRepository::save);

        return sb.toString();
    }

    @Override
    public boolean areImported() {
        return playerRepository.count() > 0;
    }

    @Override
    public String readPlayersJsonFile() throws IOException {
        return Files.readString(Path.of(PLAYERS_FILE_PATH));
    }

    @Override
    public String exportPlayersWhereSalaryBiggerThan() {
        StringBuilder sb = new StringBuilder();

        List<Player> players = playerRepository.findAllBySalaryGreaterThanOrderBySalaryDesc(BigDecimal.valueOf(100000));

        players
                .forEach(player -> {
                    sb
                            .append(String.format("Player name: %s %s", player.getFirstName(), player.getLastName()))
                            .append(System.lineSeparator())
                            .append(String.format("\tNumber: %d", player.getNumber()))
                            .append(System.lineSeparator())
                            .append(String.format("\tSalary: %.2f", player.getSalary()))
                            .append(System.lineSeparator())
                            .append(String.format("\tTeam: %s", player.getTeam().getName()))
                            .append(System.lineSeparator());
                });

        return sb.toString();
    }

    @Override
    public String exportPlayersInATeam() {
        StringBuilder sb = new StringBuilder();

        Team team = teamService.findByName("North Hub");
        List<Player> players = playerRepository.findAllByTeamOrderById(team);

        sb
                .append(String.format("Team: %s", team.getName()))
                .append(System.lineSeparator());

        players
                .forEach(player -> {
                    sb
                            .append(String.format("\tPlayer name: %s %s - %s", player.getFirstName(), player.getLastName(), player.getPosition()))
                            .append(System.lineSeparator())
                            .append(String.format("\tNumber: %d", player.getNumber()))
                            .append(System.lineSeparator());
                });

        return sb.toString();
    }
}
