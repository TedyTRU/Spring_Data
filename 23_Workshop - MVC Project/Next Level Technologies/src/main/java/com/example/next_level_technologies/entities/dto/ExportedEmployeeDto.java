package com.example.next_level_technologies.entities.dto;

import com.example.next_level_technologies.entities.models.Project;

public class ExportedEmployeeDto {

    private String firstName;
    private String lastName;
    private Integer age;
    private String projectName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Override
    public String toString() {
        return "Name: " + this.getFirstName() + " " + this.getLastName() + "\n"
                + "\tAge: " + this.getAge() + "\n"
                + "\tProject name: " + this.projectName;
    }
}
