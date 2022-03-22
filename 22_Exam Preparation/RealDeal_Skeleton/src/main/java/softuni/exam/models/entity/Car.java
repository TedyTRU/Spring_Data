package softuni.exam.models.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "cars")
public class Car extends BaseEntity {

    private String make;
    private String model;
    private Long kilometers;
    private LocalDate registeredOn;
    private Set<Picture> pictures;

    public Car() {
    }

    @Column(length = 20)
    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    @Column(length = 20)
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Column
    public Long getKilometers() {
        return kilometers;
    }

    public void setKilometers(Long kilometers) {
        this.kilometers = kilometers;
    }

    @Column(name = "registered_on")
    public LocalDate getRegisteredOn() {
        return registeredOn;
    }

    public void setRegisteredOn(LocalDate registeredOn) {
        this.registeredOn = registeredOn;
    }

    @OneToMany(mappedBy = "car")
    public Set<Picture> getPictures() {
        return pictures;
    }

    public void setPictures(Set<Picture> pictures) {
        this.pictures = pictures;
    }
}
