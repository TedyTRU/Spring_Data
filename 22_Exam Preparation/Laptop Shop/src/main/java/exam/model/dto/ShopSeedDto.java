package exam.model.dto;

import exam.model.entity.Town;

import javax.validation.constraints.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.math.BigDecimal;

@XmlAccessorType(XmlAccessType.FIELD)
public class ShopSeedDto {

    @XmlElement(name = "address")
    private String address;
    @XmlElement(name = "employee-count")
    private Integer employeeCount;
    @XmlElement(name = "income")
    private BigDecimal income;
    @XmlElement(name = "name")
    private String name;
    @XmlElement(name = "shop-area")
    private Integer shopArea;
    @XmlElement(name = "town")
    private TownNameSeedDto town;

    @Size(min = 4)
    @NotBlank
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Min(1)
    @Max(50)
    @NotNull
    public Integer getEmployeeCount() {
        return employeeCount;
    }

    public void setEmployeeCount(Integer employeeCount) {
        this.employeeCount = employeeCount;
    }

    @Min(20000)
    @NotNull
    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    @Size(min = 4)
    @NotBlank
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Min(150)
    @NotNull
    public Integer getShopArea() {
        return shopArea;
    }

    public void setShopArea(Integer shopArea) {
        this.shopArea = shopArea;
    }

    public TownNameSeedDto getTown() {
        return town;
    }

    public void setTown(TownNameSeedDto town) {
        this.town = town;
    }
}
