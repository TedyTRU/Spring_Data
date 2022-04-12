package com.example.next_level_technologies.repositories;

import com.example.next_level_technologies.entities.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Project findByNameAndCompany_Name(String name, String company_name);

    boolean existsAllBy();

    List<Project> findAllByFinishedIsTrue();

}
