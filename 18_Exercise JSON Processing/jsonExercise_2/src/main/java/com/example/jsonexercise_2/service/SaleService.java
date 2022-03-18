package com.example.jsonexercise_2.service;

import com.example.jsonexercise_2.model.dto.ex5.CustomerCarsDto;
import com.example.jsonexercise_2.model.dto.ex6.SalesDiscountDto;
import com.example.jsonexercise_2.model.entity.Sale;

import java.util.List;

public interface SaleService {

    void seedSales();

    List<SalesDiscountDto> getAllSales();

    List<CustomerCarsDto> getCustomerCars();

    List<Sale> getSalesByCustomer(Long id);
}
