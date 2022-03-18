package com.example.jsonexercise_2.service.Impl;

import com.example.jsonexercise_2.model.dto.ex1.OrderedCustomersDto;
import com.example.jsonexercise_2.model.dto.ex1.SalesDto;
import com.example.jsonexercise_2.model.dto.seed.CustomerSeedDto;
import com.example.jsonexercise_2.model.entity.Customer;
import com.example.jsonexercise_2.model.entity.Sale;
import com.example.jsonexercise_2.repository.CustomerRepository;
import com.example.jsonexercise_2.service.CustomerService;
import com.example.jsonexercise_2.service.SaleService;
import com.example.jsonexercise_2.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static com.example.jsonexercise_2.constants.GlobalConstants.RESOURCE_FILE_PATH;

@Service
public class CustomerServiceImpl implements CustomerService {

    private static final String CUSTOMERS_FILE_NAME = "customers.json";

    private final CustomerRepository customerRepository;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository, Gson gson, ValidationUtil validationUtil, ModelMapper modelMapper) {
        this.customerRepository = customerRepository;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
    }

    @Override
    public void seedCustomers() throws IOException {

        if (customerRepository.count() == 0) {
            String content = Files.readString(Path.of(RESOURCE_FILE_PATH + CUSTOMERS_FILE_NAME));

            CustomerSeedDto[] customerSeedDtos = gson.fromJson(content, CustomerSeedDto[].class);

            Arrays.stream(customerSeedDtos)
                    .filter(validationUtil::isValid)
                    .map(customerSeedDto -> modelMapper.map(customerSeedDto, Customer.class))
                    .forEach(customerRepository::save);
        }
    }

    @Override
    public long getCarsCount() {
        return customerRepository.count();
    }

    @Override
    public Customer getCustomerById(long id) {
        return customerRepository.findById(id).orElse(null);
    }

    @Override
    public List<Customer> findAllOrderedByBirthDay() {

        return customerRepository.findAll().stream()
                .sorted(Comparator.comparing(Customer::getBirthDate).thenComparing(Customer::getYoungDriver))
                .toList();
    }

}
