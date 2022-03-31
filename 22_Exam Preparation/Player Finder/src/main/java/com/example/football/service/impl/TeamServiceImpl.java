package com.example.football.service.impl;

import com.example.football.models.dto.seed.TeamSeedDto;
import com.example.football.models.entity.Team;
import com.example.football.models.entity.Town;
import com.example.football.repository.TeamRepository;
import com.example.football.service.TeamService;
import com.example.football.service.TownService;
import com.example.football.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class TeamServiceImpl implements TeamService {

    public static final String TEAMS_FILE_PATH = "src/main/resources/files/json/teams.json";

    private final TeamRepository teamRepository;
    private final TownService townService;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final Gson gson;

    public TeamServiceImpl(TeamRepository teamRepository, TownService townService, ValidationUtil validationUtil, ModelMapper modelMapper, Gson gson) {
        this.teamRepository = teamRepository;
        this.townService = townService;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.gson = gson;
    }

    @Override
    public boolean areImported() {
        return teamRepository.count() > 0;
    }

    @Override
    public String readTeamsFileContent() throws IOException {
        return Files.readString(Path.of(TEAMS_FILE_PATH));
    }

    @Override
    public String importTeams() throws IOException {
        StringBuilder sb = new StringBuilder();

        TeamSeedDto[] teamSeedDtos = gson.fromJson(readTeamsFileContent(), TeamSeedDto[].class);

        Arrays.stream(teamSeedDtos)
                .filter(teamSeedDto -> {
                    boolean isValid = validationUtil.isValid(teamSeedDto)
                            && !isTeamExistByName(teamSeedDto.getName());

                    sb.append(isValid
                                    ? String.format("Successfully imported Team %s - %d", teamSeedDto.getName(), teamSeedDto.getFanBase())
                                    : "Invalid Team")
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(teamSeedDto -> {
                    Team team = modelMapper.map(teamSeedDto, Team.class);
                    Town town = townService.findTownByName(teamSeedDto.getTownName());
                    team.setTown(town);
                    return team;
                })
                .forEach(teamRepository::save);

        return sb.toString();
    }

    @Override
    public boolean isTeamExistByName(String name) {
        return teamRepository.existsByName(name);
    }

    @Override
    public Team findTeamByName(String name) {
        return teamRepository.findTeamByName(name).orElse(null);
    }
}
