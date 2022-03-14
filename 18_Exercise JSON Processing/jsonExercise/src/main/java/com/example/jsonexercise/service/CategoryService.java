package com.example.jsonexercise.service;

import com.example.jsonexercise.model.entity.Category;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Set;

public interface CategoryService {
    void seedCategories() throws IOException;

    Set<Category> findRandomCategories();
}
