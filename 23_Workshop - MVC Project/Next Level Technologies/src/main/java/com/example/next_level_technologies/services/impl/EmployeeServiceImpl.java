package com.example.next_level_technologies.services.impl;

import com.example.next_level_technologies.entities.dto.EmployeeDto;
import com.example.next_level_technologies.entities.models.Employee;
import com.example.next_level_technologies.entities.models.Project;
import com.example.next_level_technologies.repositories.EmployeeRepository;
import com.example.next_level_technologies.services.EmployeeService;
import com.example.next_level_technologies.services.ProjectService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ProjectService projectService;
    private final ModelMapper modelMapper;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, ProjectService projectService, ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.projectService = projectService;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean exist() {
        return this.employeeRepository.existsAllBy();
    }

    @Override
    public String getXmlForImport() throws IOException {
        return new String(this.getClass().getClassLoader().getResourceAsStream(FILE_PATH).readAllBytes(), StandardCharsets.UTF_8);
    }

    @Override
    public Long create(EmployeeDto request) {

        Employee exist = employeeRepository.findFirstByFirstNameAndLastNameAndAge(request.getFirstName(), request.getLastName(), request.getAge());

        if (exist != null) {
            return exist.getId();
        }

        Employee employee = modelMapper.map(request, Employee.class);
        Long projectId = this.projectService.create(request.getProject());
        Project project = this.projectService.find(projectId);

        employee.setProject(project);
        this.employeeRepository.save(employee);

        return employee.getId();
    }
}
