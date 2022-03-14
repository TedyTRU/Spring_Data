package com.example.jsonexercise.service.Impl;

import com.example.jsonexercise.model.dto.ProductSeedDto;
import com.example.jsonexercise.model.entity.Product;
import com.example.jsonexercise.repository.ProductRepository;
import com.example.jsonexercise.service.ProductService;
import com.example.jsonexercise.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static com.example.jsonexercise.constants.GlobalConstants.RESOURCE_FILE_PATH;

@Service
public class ProductServiceImpl implements ProductService {

    private static final String PRODUCT_FILE_NAME = "products.json";

    private final ProductRepository productRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final Gson gson;

    public ProductServiceImpl(ProductRepository productRepository, ValidationUtil validationUtil, ModelMapper modelMapper, Gson gson) {
        this.productRepository = productRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.gson = gson;
    }

    @Override
    public void seedProducts() throws IOException {

        String fileContent = Files
                .readString(Path.of(RESOURCE_FILE_PATH + PRODUCT_FILE_NAME));

//        ProductSeedDto[] productSeedDtos = gson.fromJson(fileContent, ProductSeedDto[].class);
//
//        Arrays.stream(productSeedDtos)
//                .filter(validationUtil::isValid)
//                .map(productSeedDto -> {
//                    Product product = modelMapper.map(productSeedDto, Product.class);
//                })

    }

}
