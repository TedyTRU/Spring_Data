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
public class ImportController extends BaseController {

    private final ProjectService projectService;
    private final CompanyService companyService;
    private final EmployeeService employeeService;
    private final DataConverter converter;

    public ImportController(ProjectService projectService, CompanyService companyService, EmployeeService employeeService, DataConverter converter) {
        this.projectService = projectService;
        this.companyService = companyService;
        this.employeeService = employeeService;
        this.converter = converter;
    }

    @GetMapping("/import/xml")
    public String importXml(HttpServletRequest request, Model model) {

        if (!this.isLogged(request)) {
            return "redirect:/";
        }

        model.addAttribute("areImported",
                new boolean[]{this.companyService.exist(), this.employeeService.exist(), this.projectService.exist()});

        return "xml/import-xml";
    }

    @GetMapping("/import/companies")
    public String importCompanies(Model model, HttpServletRequest request) throws IOException {

        if (!this.isLogged(request)) {
            return "redirect:/";
        }

        model.addAttribute("companies", this.companyService.getXmlForImport());

        return "xml/import-companies";
    }

    @PostMapping("/import/companies")
    public String importCompanies(ImportCompaniesDto request) {

        CompanyCollectionDto companyRoot = this.converter.deserialize(request.getCompanies(), CompanyCollectionDto.class);

        companyRoot.getCompanies().forEach(this.companyService::create);

        return "redirect:/import/xml";
    }

    @GetMapping("/import/projects")
    public String importProjects(Model model, HttpServletRequest request) throws IOException {

        if (!this.isLogged(request)) {
            return "redirect:/";
        }

        model.addAttribute("projects", this.projectService.getXmlForImport());

        return "xml/import-projects";
    }

    @PostMapping("/import/projects")
    public String importProjects(ImportProjectsDto request) {

        ProjectCollectionDto projectRoot = this.converter.deserialize(request.getProjects(), ProjectCollectionDto.class);

        projectRoot.getProjects().forEach(this.projectService::create);

        return "redirect:/import/xml";
    }

    @GetMapping("/import/employees")
    public String importEmployees(Model model, HttpServletRequest request) throws IOException {

        if (!this.isLogged(request)) {
            return "redirect:/";
        }

        model.addAttribute("employees", this.employeeService.getXmlForImport());

        return "xml/import-employees";
    }

    @PostMapping("/import/employees")
    public String importEmployees(ImportEmployeesDto request) {

        EmployeeCollectionDto employeeRoot = this.converter.deserialize(request.getEmployees(), EmployeeCollectionDto.class);

        employeeRoot.getEmployees().forEach(this.employeeService::create);

        return "redirect:/import/xml";
    }

}
