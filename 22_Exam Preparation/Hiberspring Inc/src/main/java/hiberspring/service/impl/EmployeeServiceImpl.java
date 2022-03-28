package hiberspring.service.impl;

import hiberspring.domain.dtos.EmployeeDtos.EmployeeSeedRootDto;
import hiberspring.domain.entities.Branch;
import hiberspring.domain.entities.Employee;
import hiberspring.domain.entities.EmployeeCard;
import hiberspring.repository.EmployeeRepository;
import hiberspring.service.BranchService;
import hiberspring.service.EmployeeCardService;
import hiberspring.service.EmployeeService;
import hiberspring.util.ValidationUtil;
import hiberspring.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static hiberspring.common.Constants.*;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final BranchService branchService;
    private final EmployeeCardService employeeCardService;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, BranchService branchService, EmployeeCardService employeeCardService, ValidationUtil validationUtil, ModelMapper modelMapper, XmlParser xmlParser) {
        this.employeeRepository = employeeRepository;
        this.branchService = branchService;
        this.employeeCardService = employeeCardService;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
    }

    @Override
    public Boolean employeesAreImported() {
        return employeeRepository.count() > 0;
    }

    @Override
    public String readEmployeesXmlFile() throws IOException {
        return Files.readString(Path.of(EMPLOYEES_FILE_PATH));
    }

    @Override
    public String importEmployees() throws JAXBException, FileNotFoundException {
        StringBuilder sb = new StringBuilder();

        EmployeeSeedRootDto employeeSeedRootDto = xmlParser.parseXml(EmployeeSeedRootDto.class, EMPLOYEES_FILE_PATH);

        employeeSeedRootDto
                .getEmployees()
                .stream()
                .filter(employeeSeedDto -> {
                    boolean isValid = validationUtil.isValid(employeeSeedDto)
                            && !isEntityExist(employeeSeedDto.getCard())
                            && employeeCardService.isEntityExist(employeeSeedDto.getCard())
                            && branchService.isEntityExistByName(employeeSeedDto.getBranch());

                    sb
                            .append(isValid
                                    ? String.format(SUCCESSFUL_IMPORT_MESSAGE, "Employee " + employeeSeedDto.getFirstName(), employeeSeedDto.getLastName())
                                    : INCORRECT_DATA_MESSAGE)
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(employeeSeedDto -> {
                    Employee employee = modelMapper.map(employeeSeedDto, Employee.class);
                    EmployeeCard employeeCard = employeeCardService.findByCardNumber(employeeSeedDto.getCard());
                    Branch branch = branchService.findByName(employeeSeedDto.getBranch());

                    employee.setCard(employeeCard);
                    employee.setBranch(branch);
                    return employee;
                })
                .forEach(employeeRepository::save);

        return sb.toString();
    }

    @Override
    public String exportProductiveEmployees() {
        StringBuilder sb = new StringBuilder();

        List<Employee> employees = employeeRepository.findAllByBranch_ProductsOrderByFirstNameAndPositionLength();

        employees
                .forEach(employee -> {
                    sb
                            .append(String.format("Name: %s %s", employee.getFirstName(), employee.getLastName()))
                            .append(System.lineSeparator())
                            .append(String.format("Position: %s", employee.getPosition()))
                            .append(System.lineSeparator())
                            .append(String.format("Card Number: %s", employee.getCard().getNumber()))
                            .append(System.lineSeparator())
                            .append("-------------------------")
                            .append(System.lineSeparator());
                });

        return sb.toString();
    }

    @Override
    public boolean isEntityExist(String number) {
        return employeeRepository.existsByCard_Number(number);
    }
}
