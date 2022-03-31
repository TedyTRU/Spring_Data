package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.seed.TicketsSeed.TicketSeedRootDto;
import softuni.exam.models.entities.Passenger;
import softuni.exam.models.entities.Plane;
import softuni.exam.models.entities.Ticket;
import softuni.exam.models.entities.Town;
import softuni.exam.repository.TicketRepository;
import softuni.exam.service.PassengerService;
import softuni.exam.service.PlaneService;
import softuni.exam.service.TicketService;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class TicketServiceImpl implements TicketService {

    public static final String TICKETS_FILE_PATH = "src/main/resources/files/xml/tickets.xml";

    private final TicketRepository ticketRepository;
    private final TownService townService;
    private final PlaneService planeService;
    private final PassengerService passengerService;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    public TicketServiceImpl(TicketRepository ticketRepository, TownService townService, PlaneService planeService, PassengerService passengerService, XmlParser xmlParser, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.ticketRepository = ticketRepository;
        this.townService = townService;
        this.planeService = planeService;
        this.passengerService = passengerService;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return ticketRepository.count() > 0;
    }

    @Override
    public String readTicketsFileContent() throws IOException {
        return Files.readString(Path.of(TICKETS_FILE_PATH));
    }

    @Override
    public String importTickets() throws JAXBException, FileNotFoundException {
        StringBuilder sb = new StringBuilder();

        TicketSeedRootDto ticketSeedRootDto = xmlParser
                .fromFile(TICKETS_FILE_PATH, TicketSeedRootDto.class);

        ticketSeedRootDto
                .getTickets()
                .stream()
                .filter(ticketSeedDto -> {
                    boolean isValid = validationUtil.isValid(ticketSeedDto);

                    sb.append(isValid
                                    ? String.format("Successfully imported Ticket %s - %s",
                                    ticketSeedDto.getFromTown().getName(), ticketSeedDto.getToTown().getName())
                                    : "Invalid Ticket")
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(ticketSeedDto -> {
                    Ticket ticket = modelMapper.map(ticketSeedDto, Ticket.class);
                    Town fromTown = townService.findTownByName(ticketSeedDto.getFromTown().getName());
                    Town toTown = townService.findTownByName(ticketSeedDto.getToTown().getName());
                    Plane plane = planeService.findPlaneByRegNumber(ticketSeedDto.getPlane().getRegisterNumber());
                    Passenger passenger = passengerService.findByEmail(ticketSeedDto.getPassenger().getEmail());

                    ticket.setFromTown(fromTown);
                    ticket.setToTown(toTown);
                    ticket.setPlane(plane);
                    ticket.setPassenger(passenger);

                    return ticket;
                })
                .forEach(ticketRepository::save);

        return sb.toString();
    }
}
