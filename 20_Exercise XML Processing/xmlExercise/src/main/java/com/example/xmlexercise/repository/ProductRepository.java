package com.example.xmlexercise.repository;

import com.example.xmlexercise.model.entity.Category;
import com.example.xmlexercise.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByPriceIsBetweenAndBuyerIsNull(BigDecimal lower, BigDecimal upper);

}
