package com.example.jsonexercise_2.repository;

import com.example.jsonexercise_2.model.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    @Query("SELECT s FROM Supplier s WHERE s.importer = ?1 ")
    List<Supplier> findAllByIsImporter(Boolean isImporter);
}
