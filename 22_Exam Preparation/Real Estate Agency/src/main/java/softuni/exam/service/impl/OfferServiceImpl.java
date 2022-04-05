package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.OfferSeedRootDto;
import softuni.exam.models.entity.Agent;
import softuni.exam.models.entity.Apartment;
import softuni.exam.models.entity.Offer;
import softuni.exam.repository.OfferRepository;
import softuni.exam.service.AgentService;
import softuni.exam.service.ApartmentService;
import softuni.exam.service.OfferService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class OfferServiceImpl implements OfferService {

    public static final String OFFERS_FILE_PATH = "src/main/resources/files/xml/offers.xml";

    private final OfferRepository offerRepository;
    private final AgentService agentService;
    private final ApartmentService apartmentService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;

    public OfferServiceImpl(OfferRepository offerRepository, AgentService agentService, ApartmentService apartmentService, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser) {
        this.offerRepository = offerRepository;
        this.agentService = agentService;
        this.apartmentService = apartmentService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
    }

    @Override
    public boolean areImported() {
        return offerRepository.count() > 0;
    }

    @Override
    public String readOffersFileContent() throws IOException {
        return Files.readString(Path.of(OFFERS_FILE_PATH));
    }

    @Override
    public String importOffers() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();

        OfferSeedRootDto offerSeedRootDto = xmlParser.fromFile(OFFERS_FILE_PATH, OfferSeedRootDto.class);

        offerSeedRootDto
                .getOffers()
                .stream()
                .filter(offerSeedDto -> {
                    boolean isValid = validationUtil.isValid(offerSeedDto)
                            && agentService.isAgentExist(offerSeedDto.getAgent().getName());

                    sb
                            .append(isValid
                                    ? String.format("Successfully imported offer %.2f", offerSeedDto.getPrice())
                                    : "Invalid offer")
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(offerSeedDto -> {
                    Offer offer = modelMapper.map(offerSeedDto, Offer.class);
                    Agent agent = agentService.findByFirstName(offerSeedDto.getAgent().getName());
                    Apartment apartment = apartmentService.findApartmentById(offerSeedDto.getApartment().getId());

                    offer.setAgent(agent);
                    offer.setApartment(apartment);
                    return offer;
                })
                .forEach(offerRepository::save);

        return sb.toString();
    }

    @Override
    public String exportOffers() {
        StringBuilder sb = new StringBuilder();

        List<Offer> offers = offerRepository
                .findAllByApartment_ApartmentTypeOrderByAreaDescThenByPrice();

        offers.forEach(offer -> {
            sb
                    .append(String.format("Agent %s %s with offer №%d:", offer.getAgent().getFirstName(), offer.getAgent().getLastName(), offer.getId()))
                    .append(System.lineSeparator())
                    .append(String.format("\t-Apartment area: %.2f", offer.getApartment().getArea()))
                    .append(System.lineSeparator())
                    .append(String.format("\t--Town: %s", offer.getApartment().getTown().getTownName()))
                    .append(System.lineSeparator())
                    .append(String.format("\t---Price: %.2f$", offer.getPrice()))
                    .append(System.lineSeparator());
        });

        return sb.toString();
    }
}
