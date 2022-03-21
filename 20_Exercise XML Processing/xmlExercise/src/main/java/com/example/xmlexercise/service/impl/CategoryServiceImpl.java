package com.example.xmlexercise.service.impl;

import com.example.xmlexercise.model.dto.ex3.*;
import com.example.xmlexercise.model.dto.seed.CategorySeedDto;
import com.example.xmlexercise.model.entity.Category;
import com.example.xmlexercise.model.entity.Product;
import com.example.xmlexercise.repository.CategoryRepository;
import com.example.xmlexercise.service.CategoryService;
import com.example.xmlexercise.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(ModelMapper modelMapper, ValidationUtil validationUtil, CategoryRepository categoryRepository) {
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void seedCategories(List<CategorySeedDto> categories) {
        categories.stream()
                .filter(validationUtil::isValid)
                .map(categorySeedDto -> modelMapper.map(categorySeedDto, Category.class))
                .forEach(categoryRepository::save);
    }

    @Override
    public long getEntityCount() {
        return categoryRepository.count();
    }

    @Override
    public Set<Category> getRandomCategories() {
        Set<Category> categories = new HashSet<>();
        long categoriesCount = categoryRepository.count() + 1;

        for (int i = 0; i < 2; i++) {
            long randomId = ThreadLocalRandom.current().nextLong(1, categoriesCount);
            categories.add(categoryRepository.findById(randomId).orElse(null));
        }

        return categories;
    }

    @Override
    public ProductsByCategoryRootDto productDetailsByCategory() {

        ProductsByCategoryRootDto rootDto = new ProductsByCategoryRootDto();

        rootDto.setProducts(categoryRepository
                .findAllOrderedByProducts()
                .stream()
                .map(category -> {
                    ProductsByCategoryDto products = modelMapper.map(category, ProductsByCategoryDto.class);
                    setAveragePrice(category, products);
                    return products;
                }).toList()
        );

        return rootDto;
    }

    private void setAveragePrice(Category category, ProductsByCategoryDto products) {
        int count = category.getProducts().size();

        BigDecimal totalRevenue = category
                .getProducts()
                .stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal averagePrice = BigDecimal.valueOf(0L);

        if (count > 0) {
            averagePrice = totalRevenue.divide(BigDecimal.valueOf(count), RoundingMode.FLOOR);
        }

        products.setProductsCount(count);
        products.setAveragePrice(averagePrice);
        products.setTotalRevenue(totalRevenue);
    }

}
