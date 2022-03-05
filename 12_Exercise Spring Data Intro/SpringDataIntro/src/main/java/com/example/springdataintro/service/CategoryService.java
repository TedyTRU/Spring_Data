package com.example.springdataintro.service;

import com.example.springdataintro.model.entity.Category;

import java.io.IOException;
import java.util.Set;

public interface CategoryService {
    void seedCategory() throws IOException;

    Set<Category> getRandomCategories();

}
