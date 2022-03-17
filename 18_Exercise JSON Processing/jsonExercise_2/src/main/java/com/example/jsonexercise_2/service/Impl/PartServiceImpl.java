package com.example.jsonexercise_2.service.Impl;

import com.example.jsonexercise_2.model.dto.seed.PartSeedDto;
import com.example.jsonexercise_2.model.entity.Part;
import com.example.jsonexercise_2.repository.PartRepository;
import com.example.jsonexercise_2.service.PartService;
import com.example.jsonexercise_2.service.SupplierService;
import com.example.jsonexercise_2.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static com.example.jsonexercise_2.constants.GlobalConstants.RESOURCE_FILE_PATH;

@Service
public class PartServiceImpl implements PartService {

    private static final String PARTS_FILE_NAME = "parts.json";

    private final PartRepository partRepository;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final SupplierService supplierService;

    public PartServiceImpl(PartRepository partRepository, Gson gson, ValidationUtil validationUtil, ModelMapper modelMapper, SupplierService supplierService) {
        this.partRepository = partRepository;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.supplierService = supplierService;
    }

    @Override
    public void seedParts() throws IOException {

        if (partRepository.count() == 0) {

            String content = Files.readString(Path.of(RESOURCE_FILE_PATH + PARTS_FILE_NAME));

            PartSeedDto[] partSeedDtos = gson.fromJson(content, PartSeedDto[].class);

            Arrays.stream(partSeedDtos)
                    .filter(validationUtil::isValid)
                    .map(partSeedDto -> {
                        Part part = modelMapper.map(partSeedDto, Part.class);
                        part.setSupplier(supplierService.findRandomSupplier());
                        return part;
                    })
                    .forEach(partRepository::save);
        }
    }

    @Override
    public List<Part> getRandomPartList() {
        List<Part> partList = new ArrayList<>();
        int randomSize = ThreadLocalRandom.current().nextInt(3, 6);
        long repositorySize = partRepository.count() + 1;

        for (int i = 0; i < randomSize; i++) {
            long randomId = ThreadLocalRandom.current().nextLong(1, repositorySize);
            partList.add(partRepository.findById(randomId).orElse(null));
        }

        return partList;
    }

}
