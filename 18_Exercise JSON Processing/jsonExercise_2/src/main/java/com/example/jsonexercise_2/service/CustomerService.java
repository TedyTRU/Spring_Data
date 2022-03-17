package com.example.jsonexercise_2.service;

import com.example.jsonexercise_2.model.entity.Customer;

import java.io.IOException;

public interface CustomerService {

    void seedCustomers() throws IOException;

    long getCarsCount();

    Customer getCustomerById(long id);

}
