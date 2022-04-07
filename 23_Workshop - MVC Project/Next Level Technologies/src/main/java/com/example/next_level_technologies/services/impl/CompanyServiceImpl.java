package com.example.next_level_technologies.services.impl;

import com.example.next_level_technologies.entities.dto.CompanyDto;
import com.example.next_level_technologies.entities.models.Company;
import com.example.next_level_technologies.repositories.CompanyRepository;
import com.example.next_level_technologies.services.CompanyService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final ModelMapper modelMapper;

    public CompanyServiceImpl(CompanyRepository companyRepository, ModelMapper modelMapper) {
        this.companyRepository = companyRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean exist() {
        return this.companyRepository.existsAllBy();
    }

    @Override
    public String getXmlForImport() throws IOException {
        return new String(this.getClass().getClassLoader().getResourceAsStream(FILE_PATH).readAllBytes(), StandardCharsets.UTF_8);
    }

    @Override
    public Long create(CompanyDto request) {

        Company exist = this.companyRepository.findFirstByName(request.getName());

        if (exist != null) {
            return exist.getId();
        }

        Company company = this.modelMapper.map(request, Company.class);

        this.companyRepository.save(company);

        return company.getId();
    }

    @Override
    public Company find(Long id) {
        return this.companyRepository.findById(id).orElseThrow();
    }
}
