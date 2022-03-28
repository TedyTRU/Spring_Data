package hiberspring.service.impl;

import com.google.gson.Gson;
import hiberspring.domain.dtos.EmployeeCardSeedDto;
import hiberspring.domain.entities.EmployeeCard;
import hiberspring.repository.EmployeeCardRepository;
import hiberspring.service.EmployeeCardService;
import hiberspring.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static hiberspring.common.Constants.*;

@Service
public class EmployeeCardServiceImpl implements EmployeeCardService {

    private final EmployeeCardRepository employeeCardRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    public EmployeeCardServiceImpl(EmployeeCardRepository employeeCardRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.employeeCardRepository = employeeCardRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public Boolean employeeCardsAreImported() {
        return employeeCardRepository.count() > 0;
    }

    @Override
    public String readEmployeeCardsJsonFile() throws IOException {
        return Files.readString(Path.of(EMPLOYEE_CARD_FILE_PATH));
    }

    @Override
    public String importEmployeeCards(String employeeCardsFileContent) throws IOException {
        StringBuilder sb = new StringBuilder();

        EmployeeCardSeedDto[] employeeCardSeedDtos = gson.fromJson(readEmployeeCardsJsonFile(), EmployeeCardSeedDto[].class);

        Arrays.stream(employeeCardSeedDtos)
                .filter(employeeCardSeedDto -> {
                    boolean isValid = validationUtil.isValid(employeeCardSeedDto)
                            && !isEntityExist(employeeCardSeedDto.getNumber());

                    sb
                            .append(isValid
                                    ? String.format(SUCCESSFUL_IMPORT_MESSAGE, "Employee Card", employeeCardSeedDto.getNumber())
                                    : INCORRECT_DATA_MESSAGE)
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(employeeCardSeedDto -> modelMapper.map(employeeCardSeedDto, EmployeeCard.class))
                .forEach(employeeCardRepository::save);

        return sb.toString();
    }

    @Override
    public boolean isEntityExist(String number) {
        return employeeCardRepository.existsByNumber(number);
    }

    @Override
    public EmployeeCard findByCardNumber(String number) {
        return employeeCardRepository.findEmployeeCardByNumber(number);
    }
}
