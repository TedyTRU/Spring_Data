package com.example.next_level_technologies.controllers;

import com.example.next_level_technologies.services.CompanyService;
import com.example.next_level_technologies.services.EmployeeService;
import com.example.next_level_technologies.services.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController extends BaseController {

    private final ProjectService projectService;
    private final CompanyService companyService;
    private final EmployeeService employeeService;

    public HomeController(ProjectService projectService, CompanyService companyService, EmployeeService employeeService) {
        this.projectService = projectService;
        this.companyService = companyService;
        this.employeeService = employeeService;
    }

    @GetMapping("/")
    public String index(HttpServletRequest request) {
        if (this.isLogged(request)) {
            return "redirect:/home";
        }

        return "index";
    }

    @GetMapping("/home")
    public String home(HttpServletRequest request, Model model) {
        if (!this.isLogged(request)) {
            return "redirect:/";
        }

        model.addAttribute("areImported",
                this.companyService.exist()
                        && this.employeeService.exist()
                        && this.projectService.exist());

        return "home";
    }

}
