package com.example.jsonexercise_2.service;

import com.example.jsonexercise_2.model.dto.ex3.LocalSuppliersDto;
import com.example.jsonexercise_2.model.entity.Supplier;

import java.io.IOException;
import java.util.List;

public interface SupplierService {

    void seedParts() throws IOException;

    Supplier findRandomSupplier();

    List<LocalSuppliersDto> getLocalSuppliers();
}
