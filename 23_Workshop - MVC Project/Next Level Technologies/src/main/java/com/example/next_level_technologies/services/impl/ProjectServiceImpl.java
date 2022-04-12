package com.example.next_level_technologies.services.impl;

import com.example.next_level_technologies.entities.dto.ExportedProjectDto;
import com.example.next_level_technologies.entities.dto.ProjectDto;
import com.example.next_level_technologies.entities.models.Company;
import com.example.next_level_technologies.entities.models.Project;
import com.example.next_level_technologies.repositories.ProjectRepository;
import com.example.next_level_technologies.services.CompanyService;
import com.example.next_level_technologies.services.ProjectService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final CompanyService companyService;
    private final ModelMapper modelMapper;

    public ProjectServiceImpl(ProjectRepository projectRepository, CompanyService companyService, ModelMapper modelMapper) {
        this.projectRepository = projectRepository;
        this.companyService = companyService;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean exist() {
        return this.projectRepository.existsAllBy();
    }

    @Override
    public String getXmlForImport() throws IOException {
        return new String(this.getClass().getClassLoader().getResourceAsStream(FILE_PATH).readAllBytes(), StandardCharsets.UTF_8);
    }

    @Override
    public Long create(ProjectDto request) {

        Project exist = this.projectRepository.findByNameAndCompany_Name(request.getName(), request.getCompany().getName());

        if (exist != null) {
            return exist.getId();
        }

        Project project = this.modelMapper.map(request, Project.class);
        Long companyId = this.companyService.create(request.getCompany());
        Company company = this.companyService.find(companyId);

        project.setCompany(company);
        this.projectRepository.save(project);

        return project.getId();
    }

    @Override
    public Project find(Long id) {
        return this.projectRepository.findById(id).orElseThrow();
    }

    @Override
    public List<ExportedProjectDto> finishedProjects() {
        return this.projectRepository.findAllByFinishedIsTrue()
                .stream().map(p -> this.modelMapper.map(p, ExportedProjectDto.class))
                .collect(Collectors.toList());
    }
}
