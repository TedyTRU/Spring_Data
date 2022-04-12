package com.example.next_level_technologies.controllers;

import com.example.next_level_technologies.entities.dto.*;
import com.example.next_level_technologies.services.CompanyService;
import com.example.next_level_technologies.services.EmployeeService;
import com.example.next_level_technologies.services.ProjectService;
import com.example.next_level_technologies.util.DataConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
@RequestMapping("/import")
public class ImportController extends BaseController {

    private final CompanyService companyService;
    private final EmployeeService employeeService;
    private final ProjectService projectService;
    private final DataConverter converter;

    public ImportController(CompanyService companyService, EmployeeService employeeService, ProjectService projectService, DataConverter converter) {
        this.companyService = companyService;
        this.employeeService = employeeService;
        this.projectService = projectService;
        this.converter = converter;
    }

    @GetMapping("/xml")
    public String importXml(Model model, HttpServletRequest request) {
        if (!this.isLogged(request)) {
            return "redirect:/";
        }

        model.addAttribute("areImported", new boolean[]{this.companyService.exist(), this.projectService.exist(), this.employeeService.exist()});

        return "xml/import-xml";
    }

    @GetMapping("/companies")
    public String importCompanies(Model model, HttpServletRequest request) throws IOException {
        if (!this.isLogged(request)) {
            return "redirect:/";
        }

        model.addAttribute(
                "companies",
                this.companyService.getXmlForImport()
        );

        return "xml/import-companies";
    }

    @PostMapping("/companies")
    public String importCompanies(ImportCompaniesDto dto, HttpServletRequest req) {
        if (!this.isLogged(req)) {
            return "redirect:/";
        }

        CompanyCollectionDto companyRoot = this.converter.deserialize(
                dto.getCompanies(),
                CompanyCollectionDto.class
        );

        companyRoot.getCompanies().forEach(this.companyService::create);

        return "redirect:/import/xml";
    }

    @GetMapping("/projects")
    public String importProjects(Model model, HttpServletRequest request) throws IOException {
        if (!this.isLogged(request)) {
            return "redirect:/";
        }

        model.addAttribute(
                "projects",
                this.projectService.getXmlForImport()
        );

        return "xml/import-projects";
    }

    @PostMapping("/projects")
    public String importProjects(ImportProjectsDto dto, HttpServletRequest req) {
        if (!this.isLogged(req)) {
            return "redirect:/";
        }

        ProjectCollectionDto projectRoot = this.converter.deserialize(
                dto.getProjects(),
                ProjectCollectionDto.class
        );

        projectRoot.getProjects().forEach(this.projectService::create);

        return "redirect:/import/xml";
    }

    @GetMapping("/employees")
    public String importEmployees(Model model, HttpServletRequest request) throws IOException {
        if (!this.isLogged(request)) {
            return "redirect:/";
        }

        model.addAttribute(
                "employees",
                this.employeeService.getXmlForImport()
        );

        return "xml/import-employees";
    }

    @PostMapping("/employees")
    public String importEmployees(ImportEmployeesDto dto, HttpServletRequest req) {
        if (!this.isLogged(req)) {
            return "redirect:/";
        }

        EmployeeCollectionDto employeeRoot = this.converter.deserialize(
                dto.getEmployees(),
                EmployeeCollectionDto.class
        );

        employeeRoot.getEmployees().forEach(this.employeeService::create);

        return "redirect:/import/xml";
    }
}