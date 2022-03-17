package com.example.jsonexercise_2.model.entity;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "suppliers")
public class Supplier extends BaseEntity {

    private String name;
    private Boolean isImporter;
    private List<Part> partList;

    public Supplier() {
    }

    @Column
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "is_importer")
    public Boolean getImporter() {
        return isImporter;
    }

    public void setImporter(Boolean importer) {
        isImporter = importer;
    }


    @OneToMany(mappedBy = "supplier", fetch = FetchType.EAGER)
    public List<Part> getPartList() {
        return partList;
    }

    public void setPartList(List<Part> partList) {
        this.partList = partList;
    }
}
