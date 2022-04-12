package com.example.next_level_technologies.services;

import com.example.next_level_technologies.entities.dto.EmployeeDto;
import com.example.next_level_technologies.entities.dto.ExportedEmployeeDto;

import java.io.IOException;
import java.util.List;

public interface EmployeeService {

    String FILE_PATH = "files/xmls/employees.xml";

    boolean exist();

    String getXmlForImport() throws IOException;

    Long create (EmployeeDto request);

    List<ExportedEmployeeDto> getEmployeesAfterAge25();
}
