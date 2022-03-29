package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.domain.dto.TeamSeedRootDto;
import softuni.exam.domain.entities.Picture;
import softuni.exam.domain.entities.Team;
import softuni.exam.repository.TeamRepository;
import softuni.exam.service.PictureService;
import softuni.exam.service.TeamService;
import softuni.exam.util.ValidatorUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class TeamServiceImpl implements TeamService {

    public static final String TEAM_FILE_PATH = "src/main/resources/files/xml/teams.xml";

    private final TeamRepository teamRepository;
    private final PictureService pictureService;
    private final ValidatorUtil validationUtil;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;

    public TeamServiceImpl(TeamRepository teamRepository, PictureService pictureService, ValidatorUtil validationUtil, ModelMapper modelMapper, XmlParser xmlParser) {
        this.teamRepository = teamRepository;
        this.pictureService = pictureService;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
    }

    @Override
    public String importTeams() throws JAXBException, FileNotFoundException {
        StringBuilder sb = new StringBuilder();

        TeamSeedRootDto teamSeedRootDto = xmlParser.parseXml(TEAM_FILE_PATH, TeamSeedRootDto.class);

        teamSeedRootDto
                .getTeams()
                .stream()
                .filter(teamSeedDto -> {
                    boolean isValid = validationUtil.isValid(teamSeedDto)
                            && pictureService.isEntityExist(teamSeedDto.getPicture().getUrl());

                    sb
                            .append(isValid
                                    ? String.format("Successfully imported - %s", teamSeedDto.getName())
                                    : "Invalid team")
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(teamSeedDto -> {
                    Team team = modelMapper.map(teamSeedDto, Team.class);
                    Picture picture = pictureService.findByUrl(teamSeedDto.getPicture().getUrl());

                    team.setPicture(picture);
                    return team;
                })
                .forEach(teamRepository::save);

        return sb.toString();
    }

    @Override
    public boolean areImported() {
        return teamRepository.count() > 0;
    }

    @Override
    public String readTeamsXmlFile() throws IOException {
        return Files.readString(Path.of(TEAM_FILE_PATH));
    }

    @Override
    public boolean isEntityExist(String name) {
        return teamRepository.existsByName(name);
    }

    @Override
    public Team findByName(String name) {
        return teamRepository.findTeamByName(name);
    }
}
