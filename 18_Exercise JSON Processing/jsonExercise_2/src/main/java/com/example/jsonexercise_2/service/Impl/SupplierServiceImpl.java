package com.example.jsonexercise_2.service.Impl;

import com.example.jsonexercise_2.model.dto.ex3.LocalSuppliersDto;
import com.example.jsonexercise_2.model.dto.seed.SupplierSeedDto;
import com.example.jsonexercise_2.model.entity.Supplier;
import com.example.jsonexercise_2.repository.SupplierRepository;
import com.example.jsonexercise_2.service.SupplierService;
import com.example.jsonexercise_2.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static com.example.jsonexercise_2.constants.GlobalConstants.RESOURCE_FILE_PATH;

@Service
public class SupplierServiceImpl implements SupplierService {

    private static final String SUPPLIERS_FILE_NAME = "suppliers.json";

    private final SupplierRepository supplierRepository;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    public SupplierServiceImpl(SupplierRepository supplierRepository, Gson gson, ValidationUtil validationUtil, ModelMapper modelMapper) {
        this.supplierRepository = supplierRepository;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
    }

    @Override
    public void seedParts() throws IOException {

        if (supplierRepository.count() == 0) {
            String content = Files.readString(Path.of(RESOURCE_FILE_PATH + SUPPLIERS_FILE_NAME));

            SupplierSeedDto[] supplierSeedDtos = gson.fromJson(content, SupplierSeedDto[].class);

            Arrays.stream(supplierSeedDtos)
                    .filter(validationUtil::isValid)
                    .map(customerSeedDto -> modelMapper.map(customerSeedDto, Supplier.class))
                    .forEach(supplierRepository::save);
        }
    }

    @Override
    public Supplier findRandomSupplier() {
        long randomId = ThreadLocalRandom.current().nextLong(1, supplierRepository.count() + 1);
        return supplierRepository.findById(randomId).orElse(null);
    }

    @Override
    public List<LocalSuppliersDto> getLocalSuppliers() {

        return supplierRepository.findAllByIsImporter(false)
                .stream()
                .map(supplier -> {
                    LocalSuppliersDto localSuppliersDto = modelMapper.map(supplier, LocalSuppliersDto.class);
                    localSuppliersDto.setPartsCount(supplier.getPartList().size());
                    return localSuppliersDto;
                })
                .toList();
    }

}
