package com.example.xmlexercise.repository;

import com.example.xmlexercise.model.entity.Category;
import com.example.xmlexercise.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c FROM Category c " +
            "ORDER BY c.products.size DESC")
    List<Category> findAllOrderedByProducts();

}
