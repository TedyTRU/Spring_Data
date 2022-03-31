package com.example.football.service.impl;

import com.example.football.models.dto.seed.playerSeedDto.PlayerSeedRootDto;
import com.example.football.models.entity.Player;
import com.example.football.models.entity.Stat;
import com.example.football.models.entity.Team;
import com.example.football.models.entity.Town;
import com.example.football.repository.PlayerRepository;
import com.example.football.service.PlayerService;
import com.example.football.service.StatService;
import com.example.football.service.TeamService;
import com.example.football.service.TownService;
import com.example.football.util.ValidationUtil;
import com.example.football.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService {

    public static final String PLAYERS_FILES_PATH = "src/main/resources/files/xml/players.xml";

    private final PlayerRepository playerRepository;
    private final TownService townService;
    private final TeamService teamService;
    private final StatService statService;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;

    public PlayerServiceImpl(PlayerRepository playerRepository, TownService townService, TeamService teamService, StatService statService, ValidationUtil validationUtil, ModelMapper modelMapper, XmlParser xmlParser) {
        this.playerRepository = playerRepository;
        this.townService = townService;
        this.teamService = teamService;
        this.statService = statService;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
    }

    @Override
    public boolean areImported() {
        return playerRepository.count() > 0;
    }

    @Override
    public String readPlayersFileContent() throws IOException {
        return Files.readString(Path.of(PLAYERS_FILES_PATH));
    }

    @Override
    public String importPlayers() throws JAXBException, FileNotFoundException {
        StringBuilder sb = new StringBuilder();

        PlayerSeedRootDto playerSeedRootDto = xmlParser.fromFile(PLAYERS_FILES_PATH, PlayerSeedRootDto.class);

        playerSeedRootDto
                .getPlayers()
                .stream()
                .filter(playerSeedDto -> {
                    boolean isValid = validationUtil.isValid(playerSeedDto)
                            && !isPlayerExistByEmail(playerSeedDto.getEmail());

                    sb
                            .append(isValid
                                    ? String.format("Successfully imported Player %s %s - %s",
                                    playerSeedDto.getFirstName(), playerSeedDto.getLastName(), playerSeedDto.getPosition())
                                    : "Invalid Player")
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(playerSeedDto -> {
                    Player player = modelMapper.map(playerSeedDto, Player.class);
                    Town town = townService.findTownByName(playerSeedDto.getTownName().getName());
                    Team team = teamService.findTeamByName(playerSeedDto.getTeamName().getName());
                    Stat stat = statService.findStatById(playerSeedDto.getStatId().getId());

                    player.setTown(town);
                    player.setTeam(team);
                    player.setStat(stat);

                    return player;
                })
                .forEach(playerRepository::save);

        return sb.toString();
    }

    @Override
    public String exportBestPlayers() {
        StringBuilder sb = new StringBuilder();

        LocalDate after = LocalDate.parse("1995-01-01");
        LocalDate before = LocalDate.parse("2003-01-01");

        List<Player> players = playerRepository.findAllByBirthDateBetweenOrderByStat(after, before);

        players
                .forEach(player -> {
                    sb
                            .append(String.format("Player - %s %s", player.getFirstName(), player.getLastName()))
                            .append(System.lineSeparator())
                            .append(String.format("\tPosition - %s", player.getPosition().toString()))
                            .append(System.lineSeparator())
                            .append(String.format("\tTeam - %s", player.getTeam().getName()))
                            .append(System.lineSeparator())
                            .append(String.format("\tStadium - %s", player.getTeam().getStadiumName()))
                            .append(System.lineSeparator());
                });

        return sb.toString();
    }

    @Override
    public boolean isPlayerExistByEmail(String email) {
        return playerRepository.existsByEmail(email);
    }
}
