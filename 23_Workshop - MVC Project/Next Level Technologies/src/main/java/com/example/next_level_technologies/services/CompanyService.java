package com.example.next_level_technologies.services;

import com.example.next_level_technologies.entities.dto.CompanyDto;
import com.example.next_level_technologies.entities.models.Company;

import java.io.IOException;

public interface CompanyService {

    String FILE_PATH = "files/xmls/companies.xml";

    boolean exist();

    String getXmlForImport() throws IOException;

    Long create(CompanyDto request);

    Company find(Long id);
}
