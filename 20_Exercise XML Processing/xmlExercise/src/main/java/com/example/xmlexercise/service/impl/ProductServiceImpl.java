package com.example.xmlexercise.service.impl;

import com.example.xmlexercise.model.dto.ex1.ProductViewRootDto;
import com.example.xmlexercise.model.dto.ex1.ProductWithSellerDto;
import com.example.xmlexercise.model.dto.seed.ProductSeedDto;
import com.example.xmlexercise.model.entity.Product;
import com.example.xmlexercise.repository.ProductRepository;
import com.example.xmlexercise.service.CategoryService;
import com.example.xmlexercise.service.ProductService;
import com.example.xmlexercise.service.UserService;
import com.example.xmlexercise.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final ProductRepository productRepository;
    private final UserService userService;
    private final CategoryService categoryService;

    public ProductServiceImpl(ModelMapper modelMapper, ValidationUtil validationUtil, ProductRepository productRepository, UserService userService, CategoryService categoryService) {
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.productRepository = productRepository;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @Override
    public long getEntityCount() {
        return productRepository.count();
    }

    @Override
    public void seedProducts(List<ProductSeedDto> products) {

        products.stream()
                .filter(validationUtil::isValid)
                .map(productSeedDto -> {
                    Product product = modelMapper.map(productSeedDto, Product.class);
                    product.setSeller(userService.getRandomUser());
                    if (product.getPrice().compareTo(BigDecimal.valueOf(700L)) > 0) {
                        product.setBuyer(userService.getRandomUser());
                    }
                    product.setCategories(categoryService.getRandomCategories());
                    return product;
                })
                .forEach(productRepository::save);
    }

    @Override
    public ProductViewRootDto findProductsInRangeWithoutBuyer() {
        ProductViewRootDto rootDto = new ProductViewRootDto();

        rootDto.setProducts(productRepository
                .findAllByPriceIsBetweenAndBuyerIsNull(BigDecimal.valueOf(500L), BigDecimal.valueOf(1000L))
                .stream()
                .map(product -> {
                    ProductWithSellerDto productsDto = modelMapper.map(product, ProductWithSellerDto.class);
                    productsDto.setSeller(String.format("%s %s", product.getSeller().getFirstName(),
                            product.getSeller().getLastName()));
                    return productsDto;
                }).toList());


        return rootDto;
    }


}
