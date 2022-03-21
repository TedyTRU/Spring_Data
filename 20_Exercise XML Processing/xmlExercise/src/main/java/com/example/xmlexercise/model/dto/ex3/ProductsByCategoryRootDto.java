package com.example.xmlexercise.model.dto.ex3;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "categories")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductsByCategoryRootDto {

    @XmlElement(name = "category")
    private List<ProductsByCategoryDto> products;

    public ProductsByCategoryRootDto() {
    }

    public List<ProductsByCategoryDto> getProducts() {
        return products;
    }

    public void setProducts(List<ProductsByCategoryDto> products) {
        this.products = products;
    }
}
