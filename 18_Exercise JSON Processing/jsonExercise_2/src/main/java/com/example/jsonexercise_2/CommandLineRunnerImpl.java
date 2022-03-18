package com.example.jsonexercise_2;

import com.example.jsonexercise_2.model.dto.ex1.OrderedCustomersDto;
import com.example.jsonexercise_2.model.dto.ex1.SalesDto;
import com.example.jsonexercise_2.model.dto.ex2.CarsPrintDto;
import com.example.jsonexercise_2.model.dto.ex3.LocalSuppliersDto;
import com.example.jsonexercise_2.model.dto.ex4.CarsWithPartsDto;
import com.example.jsonexercise_2.model.dto.ex5.CustomerCarsDto;
import com.example.jsonexercise_2.model.dto.ex6.SalesDiscountDto;
import com.example.jsonexercise_2.model.entity.Sale;
import com.example.jsonexercise_2.service.*;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private static final String OUTPUT_FILES_PATH = "src/main/resources/files/out/";
    private static final String ORDERED_CUSTOMERS_FILE_NAME = "ordered-customers.json";
    private static final String CARS_FROM_MAKE_FILE_NAME = "cars-from-make.json";
    private static final String LOCAL_SUPPLIERS_FILE_NAME = "local-suppliers.json";
    private static final String CARS_WITH_THEIR_PARTS_FILE_NAME = "cars-with-their-parts.json";
    private static final String TOTAL_SALES_BY_CUSTOMERS_FILE_NAME = "total-sales-by-customers.json";
    private static final String SALES_WITH_DISCOUNTS_FILE_NAME = "sales-with-discounts.json";

    private final CarService carService;
    private final CustomerService customerService;
    private final PartService partService;
    private final SupplierService supplierService;
    private final SaleService saleService;
    private final BufferedReader bufferedReader;
    private final Gson gson;
    private final ModelMapper modelMapper;

    public CommandLineRunnerImpl(CarService carService, CustomerService customerService, PartService partService, SupplierService supplierService, SaleService saleService, BufferedReader bufferedReader, Gson gson, ModelMapper modelMapper) {
        this.carService = carService;
        this.customerService = customerService;
        this.partService = partService;
        this.supplierService = supplierService;
        this.saleService = saleService;
        this.bufferedReader = bufferedReader;
        this.gson = gson;
        this.modelMapper = modelMapper;
    }

    @Override
    public void run(String... args) throws Exception {
        seedData();

        while (true) {
            System.out.println("Please entre exercise number: ");
            int exNum = Integer.parseInt(bufferedReader.readLine());

            switch (exNum) {

                case 1 -> getAllCustomers();
                case 2 -> carsFromMakeToyota();
                case 3 -> getLocalSuppliers();
                case 4 -> getCarsWithTheirListOfParts();
                case 5 -> getTotalSalesByCustomer();
                case 6 -> salesWithAppliedDiscount();

                case 99 -> System.exit(0);
                default -> System.out.println("Please enter valid exercise number");
            }
        }

    }

    private void salesWithAppliedDiscount() throws IOException {
        List<SalesDiscountDto> salesDiscountDtos = saleService.getAllSales();
        String content = gson.toJson(salesDiscountDtos);
        writeToFile(OUTPUT_FILES_PATH + SALES_WITH_DISCOUNTS_FILE_NAME, content);
    }

    private void getTotalSalesByCustomer() throws IOException {
        List<CustomerCarsDto> customerCarsDtos = saleService.getCustomerCars();
        String content = gson.toJson(customerCarsDtos);
        writeToFile(OUTPUT_FILES_PATH + TOTAL_SALES_BY_CUSTOMERS_FILE_NAME, content);
    }

    private void getCarsWithTheirListOfParts() throws IOException {
        List<CarsWithPartsDto> carsWithPartsDtos = carService.getCarsWithParts();
        String content = gson.toJson(carsWithPartsDtos);
        writeToFile(OUTPUT_FILES_PATH + CARS_WITH_THEIR_PARTS_FILE_NAME, content);
    }

    private void getLocalSuppliers() throws IOException {
        List<LocalSuppliersDto> localSuppliersDtos = supplierService.getLocalSuppliers();
        String content = gson.toJson(localSuppliersDtos);
        writeToFile(OUTPUT_FILES_PATH + LOCAL_SUPPLIERS_FILE_NAME, content);
    }

    private void carsFromMakeToyota() throws IOException {
        List<CarsPrintDto> carsPrintDtos = carService.getCarByMake("Toyota");
        String content = gson.toJson(carsPrintDtos);
        writeToFile(OUTPUT_FILES_PATH + CARS_FROM_MAKE_FILE_NAME, content);
    }

    private void getAllCustomers() throws IOException {

        List<OrderedCustomersDto> orderedCustomersDtos = customerService.findAllOrderedByBirthDay().stream()
                .map(customer -> {
                    OrderedCustomersDto orderedCustomersDto = modelMapper.map(customer, OrderedCustomersDto.class);
                    List<Sale> sales = saleService.getSalesByCustomer(customer.getId());
                    SalesDto salesDto = modelMapper.map(sales, SalesDto.class);
                    orderedCustomersDto.setSales(salesDto);
                    return orderedCustomersDto;
                }).toList();

        String content = gson.toJson(orderedCustomersDtos);
        writeToFile(OUTPUT_FILES_PATH + ORDERED_CUSTOMERS_FILE_NAME, content);
    }

    private void writeToFile(String filePath, String content) throws IOException {
        Files.write(Path.of(filePath), Collections.singleton(content));
    }

    private void seedData() throws IOException {
        supplierService.seedParts();
        partService.seedParts();
        carService.seedCars();
        customerService.seedCustomers();
        saleService.seedSales();
    }

}
