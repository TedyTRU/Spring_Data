package com.example.next_level_technologies.repositories;

import com.example.next_level_technologies.entities.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Employee findFirstByFirstNameAndLastNameAndAge(String firstName, String lastName, Integer age);

    boolean existsAllBy();

    List<Employee> findAllByAgeAfter(Integer age);
}
