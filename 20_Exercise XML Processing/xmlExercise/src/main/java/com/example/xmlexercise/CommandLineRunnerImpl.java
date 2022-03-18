package com.example.xmlexercise;

import com.example.xmlexercise.service.CategoryService;
import com.example.xmlexercise.util.XmlParser;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private static final String RESOURCES_FILE_PATH = "src/main/resources/files/";
    private static final String CATEGORIES_FILE_NAME = "categories.xml";

    private final XmlParser xmlParser;
    private final CategoryService categoryService;

    public CommandLineRunnerImpl(XmlParser xmlParser, CategoryService categoryService) {
        this.xmlParser = xmlParser;
        this.categoryService = categoryService;
    }

    @Override
    public void run(String... args) throws Exception {
        seedData();
    }

    private void seedData() {

    }
}
