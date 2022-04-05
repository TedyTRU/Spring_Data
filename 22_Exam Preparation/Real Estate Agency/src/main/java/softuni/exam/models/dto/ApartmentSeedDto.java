package softuni.exam.models.dto;

import softuni.exam.models.entity.ApartmentType;
import softuni.exam.models.entity.Town;

import javax.persistence.Column;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class ApartmentSeedDto {

    @XmlElement(name = "apartmentType")
    private ApartmentType apartmentType;
    @XmlElement(name = "area")
    private Double area;
    @XmlElement(name = "town")
    private String town;

    @NotNull
    public ApartmentType getApartmentType() {
        return apartmentType;
    }

    public void setApartmentType(ApartmentType apartmentType) {
        this.apartmentType = apartmentType;
    }

    @DecimalMin(value = "40")
    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    @Size(min = 2)
    @Column(unique = true)
    @NotBlank
    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }
}
