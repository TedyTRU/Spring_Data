package com.example.xmlexercise;

import com.example.xmlexercise.model.dto.ex1.ProductViewRootDto;
import com.example.xmlexercise.model.dto.ex2.UserViewRootDto;
import com.example.xmlexercise.model.dto.ex3.ProductsByCategoryRootDto;
import com.example.xmlexercise.model.dto.ex4.UsersRootDto;
import com.example.xmlexercise.model.dto.seed.CategorySeedRootDto;
import com.example.xmlexercise.model.dto.seed.ProductSeedRootDto;
import com.example.xmlexercise.model.dto.seed.UserSeedRootDto;
import com.example.xmlexercise.service.CategoryService;
import com.example.xmlexercise.service.ProductService;
import com.example.xmlexercise.service.UserService;
import com.example.xmlexercise.util.XmlParser;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private static final String RESOURCES_FILE_PATH = "src/main/resources/files/";
    private static final String OUTPUT_FILE_PATH = "src/main/resources/files/out/";
    private static final String CATEGORIES_FILE_NAME = "categories.xml";
    private static final String USERS_FILE_NAME = "users.xml";
    private static final String PRODUCTS_FILE_NAME = "products.xml";
    private static final String PRODUCTS_IN_RANGE_FILE_NAME = "products-in-range.xml";
    private static final String SUCCESSFULLY_SOLD_PRODUCTS_FILE_NAME = "successfully-sold-products.xml";
    private static final String CATEGORIES_BY_PRODUCTS_COUNT_FILE_NAME = "categories-by-products-count.xml";
    private static final String USERS_AND_PRODUCTS_FILE_NAME = "users-and-products.xml";

    private final XmlParser xmlParser;
    private final CategoryService categoryService;
    private final UserService userService;
    private final ProductService productService;
    private final BufferedReader bufferedReader;

    public CommandLineRunnerImpl(XmlParser xmlParser, CategoryService categoryService, UserService userService, ProductService productService) {
        this.xmlParser = xmlParser;
        this.categoryService = categoryService;
        this.userService = userService;
        this.productService = productService;
        this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run(String... args) throws Exception {
        seedData();

        System.out.println("Please enter exercise number: ");
        int exNum = Integer.parseInt(bufferedReader.readLine());

        switch (exNum) {
            case 1 -> productsInRange();
            case 2 -> successfullySoldProducts();
            case 3 -> categoriesByProductsCount();
            case 4 -> usersAndProducts();
            case 99 -> System.exit(0);
            default -> System.out.println("Please enter valid exercise number");
        }

    }

    private void usersAndProducts() throws JAXBException {
        UsersRootDto users = userService.findAllUsersWithProducts();

        xmlParser.writeToFile(OUTPUT_FILE_PATH + USERS_AND_PRODUCTS_FILE_NAME, users);
    }

    private void categoriesByProductsCount() throws JAXBException {
        ProductsByCategoryRootDto products = categoryService
                .productDetailsByCategory();

        xmlParser.writeToFile(OUTPUT_FILE_PATH + CATEGORIES_BY_PRODUCTS_COUNT_FILE_NAME, products);
    }

    private void successfullySoldProducts() throws JAXBException {
        UserViewRootDto userViewRootDto = userService
                .findUsersWithMoreThanOneSoldProducts();

        xmlParser.writeToFile(OUTPUT_FILE_PATH + SUCCESSFULLY_SOLD_PRODUCTS_FILE_NAME, userViewRootDto);
    }

    private void productsInRange() throws JAXBException {
        ProductViewRootDto productDto = productService.findProductsInRangeWithoutBuyer();

        xmlParser.writeToFile(OUTPUT_FILE_PATH + PRODUCTS_IN_RANGE_FILE_NAME, productDto);
    }

    private void seedData() throws JAXBException, FileNotFoundException {

        if (categoryService.getEntityCount() == 0) {
            CategorySeedRootDto categorySeedRootDto = xmlParser
                    .fromFile(RESOURCES_FILE_PATH + CATEGORIES_FILE_NAME, CategorySeedRootDto.class);

            categoryService.seedCategories(categorySeedRootDto.getCategories());
        }

        if (userService.getEntityCount() == 0) {
            UserSeedRootDto userSeedRootDto = xmlParser
                    .fromFile(RESOURCES_FILE_PATH + USERS_FILE_NAME, UserSeedRootDto.class);

            userService.seedUsers(userSeedRootDto.getUsers());
        }

        if (productService.getEntityCount() == 0) {
            ProductSeedRootDto productSeedRootDto = xmlParser
                    .fromFile(RESOURCES_FILE_PATH + PRODUCTS_FILE_NAME, ProductSeedRootDto.class);

            productService.seedProducts(productSeedRootDto.getProducts());
        }

    }
}
