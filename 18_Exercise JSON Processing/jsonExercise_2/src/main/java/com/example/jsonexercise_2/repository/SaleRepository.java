package com.example.jsonexercise_2.repository;

import com.example.jsonexercise_2.model.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    List<Sale> findAll();

    @Query("SELECT s FROM Sale s GROUP BY s.customer.id")
    List<Sale> findGroupedByCustomer();

    List<Sale> findAllByCustomer_Id(Long customer_id);
}
