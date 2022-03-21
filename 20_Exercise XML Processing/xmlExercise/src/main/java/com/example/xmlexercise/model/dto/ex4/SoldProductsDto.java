package com.example.xmlexercise.model.dto.ex4;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "sold-products")
@XmlAccessorType(XmlAccessType.FIELD)
public class SoldProductsDto {

    @XmlAttribute(name = "count")
    private Integer count;
    @XmlElement(name = "product")
    private List<ProductsDto> products;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<ProductsDto> getProducts() {
        return products;
    }

    public void setProducts(List<ProductsDto> products) {
        this.products = products;
    }
}
