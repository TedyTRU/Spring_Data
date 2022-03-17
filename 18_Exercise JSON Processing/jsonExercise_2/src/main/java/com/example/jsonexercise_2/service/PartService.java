package com.example.jsonexercise_2.service;

import com.example.jsonexercise_2.model.entity.Part;

import java.io.IOException;
import java.util.List;

public interface PartService {

    void seedParts() throws IOException;

    List<Part> getRandomPartList();
}
