package com.example.xmlexercise.service;

import com.example.xmlexercise.model.dto.ex1.ProductViewRootDto;
import com.example.xmlexercise.model.dto.seed.ProductSeedDto;
import com.example.xmlexercise.model.dto.seed.ProductSeedRootDto;

import java.util.List;

public interface ProductService {

    long getEntityCount();

    void seedProducts(List<ProductSeedDto> products);

    ProductViewRootDto findProductsInRangeWithoutBuyer();
}
