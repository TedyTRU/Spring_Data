package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ApartmentSeedRootDto;
import softuni.exam.models.entity.Apartment;
import softuni.exam.models.entity.Town;
import softuni.exam.repository.ApartmentRepository;
import softuni.exam.service.ApartmentService;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class ApartmentServiceImpl implements ApartmentService {

    public static final String APARTMENTS_FILE_PATH = "src/main/resources/files/xml/apartments.xml";

    private final ApartmentRepository apartmentRepository;
    private final TownService townService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;

    public ApartmentServiceImpl(ApartmentRepository apartmentRepository, TownService townService, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser) {
        this.apartmentRepository = apartmentRepository;
        this.townService = townService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
    }


    @Override
    public boolean areImported() {
        return apartmentRepository.count() > 0;
    }

    @Override
    public String readApartmentsFromFile() throws IOException {
        return Files.readString(Path.of(APARTMENTS_FILE_PATH));
    }

    @Override
    public String importApartments() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();

        ApartmentSeedRootDto apartmentSeedRootDto = xmlParser.fromFile(APARTMENTS_FILE_PATH, ApartmentSeedRootDto.class);

        apartmentSeedRootDto
                .getApartments()
                .stream()
                .filter(apartmentSeedDto -> {
                    boolean isValid = validationUtil.isValid(apartmentSeedDto)
                            && !isApartmentExistByAreaAndTown(apartmentSeedDto.getArea(), apartmentSeedDto.getTown());

                    sb
                            .append(isValid
                                    ? String.format("Successfully imported apartment %s - %.2f", apartmentSeedDto.getApartmentType(), apartmentSeedDto.getArea())
                                    : "Invalid apartment")
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(apartmentSeedDto -> {
                    Apartment apartment = modelMapper.map(apartmentSeedDto, Apartment.class);
                    Town town = townService.getTownByName(apartmentSeedDto.getTown());
                    apartment.setTown(town);

                    return apartment;
                })
                .forEach(apartmentRepository::save);


        return sb.toString();
    }

    @Override
    public boolean isApartmentExistByAreaAndTown(Double area, String name) {
        return apartmentRepository.existsByAreaAndTown_TownName(area, name);
    }

    @Override
    public Apartment findApartmentById(Long id) {
        return apartmentRepository.findApartmentById(id).orElse(null);
    }

}
