package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.seed.PassengerSeedDto;
import softuni.exam.models.entities.Passenger;
import softuni.exam.repository.PassengerRepository;
import softuni.exam.service.PassengerService;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class PassengerServiceImpl implements PassengerService {

    public static final String PASSENGERS_FILE_PATH = "src/main/resources/files/json/passengers.json";

    private final PassengerRepository passengerRepository;
    private final TownService townService;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    public PassengerServiceImpl(PassengerRepository passengerRepository, TownService townService, Gson gson, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.passengerRepository = passengerRepository;
        this.townService = townService;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return passengerRepository.count() > 0;
    }

    @Override
    public String readPassengersFileContent() throws IOException {
        return Files.readString(Path.of(PASSENGERS_FILE_PATH));
    }

    @Override
    public String importPassengers() throws IOException {
        StringBuilder sb = new StringBuilder();

        PassengerSeedDto[] passengerSeedDtos = gson.fromJson(readPassengersFileContent(), PassengerSeedDto[].class);

        Arrays.stream(passengerSeedDtos)
                .filter(passengerSeedDto -> {
                    boolean isValid = validationUtil.isValid(passengerSeedDto);

                    sb.append(isValid
                                    ? String.format("Successfully imported Passenger %s - %s",
                                    passengerSeedDto.getLastName(), passengerSeedDto.getEmail())
                                    : "Invalid Passenger")
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(passengerSeedDto -> {
                    Passenger passenger = modelMapper.map(passengerSeedDto, Passenger.class);
                    passenger.setTown(townService.findTownByName(passengerSeedDto.getTown()));

                    return passenger;
                })
                .forEach(passengerRepository::save);

        return sb.toString();
    }

    @Override
    public String getPassengersOrderByTicketsCountDescendingThenByEmail() {
        StringBuilder sb = new StringBuilder();

        passengerRepository
                .findAllOrderByTicketsCountDescThenByEmail()
                .forEach(passenger -> {
                    sb
                            .append(String.format("Passenger %s  %s", passenger.getFirstName(), passenger.getLastName()))
                            .append(System.lineSeparator())
                            .append(String.format("\tEmail - %s", passenger.getEmail()))
                            .append(System.lineSeparator())
                            .append(String.format("\tPhone - %s", passenger.getPhoneNumber()))
                            .append(System.lineSeparator())
                            .append(String.format("\tNumber of tickets - %d", passenger.getTickets().size()))
                            .append(System.lineSeparator());
                });

        return sb.toString();
    }

    @Override
    public Passenger findByEmail(String email) {
        return passengerRepository.findPassengerByEmail(email);
    }
}
