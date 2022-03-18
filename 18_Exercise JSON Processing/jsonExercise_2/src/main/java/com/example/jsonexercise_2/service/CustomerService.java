package com.example.jsonexercise_2.service;

import com.example.jsonexercise_2.model.entity.Customer;

import java.io.IOException;
import java.util.List;

public interface CustomerService {

    void seedCustomers() throws IOException;

    long getCarsCount();

    Customer getCustomerById(long id);

    List<Customer> findAllOrderedByBirthDay();
}
