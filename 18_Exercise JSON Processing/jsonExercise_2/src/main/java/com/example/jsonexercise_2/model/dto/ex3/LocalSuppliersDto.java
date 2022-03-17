package com.example.jsonexercise_2.model.dto.ex3;

import com.google.gson.annotations.Expose;

public class LocalSuppliersDto {

    @Expose
    private Long id;
    @Expose
    private String name;
    @Expose
    private Integer partsCount;

    public LocalSuppliersDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPartsCount() {
        return partsCount;
    }

    public void setPartsCount(Integer partsCount) {
        this.partsCount = partsCount;
    }
}
