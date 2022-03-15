package com.example.jsonexercise.repository;

import com.example.jsonexercise.model.dto.categoriesByProductsCount.CategoriesByProductsDto;
import com.example.jsonexercise.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT new com.example.jsonexercise.model.dto.categoriesByProductsCount.CategoriesByProductsDto" +
            "(c.name, c.products.size, AVG(p.price), SUM(p.price)) " +
            "FROM Category c " +
            "JOIN  c.products p " +
            "GROUP BY c.id " +
            "ORDER BY c.products.size DESC")
    List<CategoriesByProductsDto> getCategoriesByProductsCount();
}
