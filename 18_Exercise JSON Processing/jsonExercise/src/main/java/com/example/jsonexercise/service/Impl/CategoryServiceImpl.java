package com.example.jsonexercise.service.Impl;

import com.example.jsonexercise.constants.GlobalConstants;
import com.example.jsonexercise.model.dto.categoriesByProductsCount.CategoriesByProductsDto;
import com.example.jsonexercise.model.dto.seed.CategorySeedDto;
import com.example.jsonexercise.model.entity.Category;
import com.example.jsonexercise.repository.CategoryRepository;
import com.example.jsonexercise.service.CategoryService;
import com.example.jsonexercise.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class CategoryServiceImpl implements CategoryService {

    private static final String CATEGORIES_FILE_NAME = "categories.json";

    private final CategoryRepository categoryRepository;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, Gson gson, ValidationUtil validationUtil, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
    }

    @Override
    public void seedCategories() throws IOException {

        if (categoryRepository.count() > 0) {
            return;
        }

        String fileContent = Files
                .readString(Path.of(GlobalConstants.RESOURCE_FILE_PATH + CATEGORIES_FILE_NAME));

        CategorySeedDto[] categorySeedDtos = gson
                .fromJson(fileContent, CategorySeedDto[].class);

        Arrays.stream(categorySeedDtos)
                .filter(validationUtil::isValid)
                .map(categorySeedDto -> modelMapper.map(categorySeedDto, Category.class))
                .forEach(categoryRepository::save);

    }

    @Override
    public Set<Category> findRandomCategories() {

        Set<Category> categorySet = new HashSet<>();

        int categoryCount = ThreadLocalRandom
                .current().nextInt(1, 4);

        long totalCategoriesCount = categoryRepository.count() + 1;

        for (int i = 0; i < categoryCount; i++) {
            long randomId = ThreadLocalRandom
                    .current().nextLong(1, totalCategoriesCount);

            categorySet.add(categoryRepository.findById(randomId).orElse(null));

        }
        return categorySet;
    }

    @Override
    public List<CategoriesByProductsDto> productDetailsByCategory() {

        return categoryRepository.getCategoriesByProductsCount();

    }

}
