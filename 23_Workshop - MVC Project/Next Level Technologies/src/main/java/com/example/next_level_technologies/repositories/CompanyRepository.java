package com.example.next_level_technologies.repositories;

import com.example.next_level_technologies.entities.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    Company findFirstByName(String name);

    boolean existsAllBy();
}
