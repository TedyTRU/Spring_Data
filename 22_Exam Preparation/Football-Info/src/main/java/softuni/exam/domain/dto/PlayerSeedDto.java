package softuni.exam.domain.dto;

import com.google.gson.annotations.Expose;

import javax.validation.constraints.*;
import java.math.BigDecimal;

public class PlayerSeedDto {

    @Expose
    private String firstName;
    @Expose
    private String lastName;
    @Expose
    private Integer number;
    @Expose
    private BigDecimal salary;
    @Expose
    private String position;
    @Expose
    private PicturePlayerDto picture;
    @Expose
    private TeamPLayerDto team;

    @NotBlank
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Size(min = 3, max = 15)
    @NotBlank
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Min(1)
    @Max(99)
    @NotNull
    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @DecimalMin(value = "0")
    @NotNull
    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    @NotBlank
    @Size(min = 2, max = 2)
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public PicturePlayerDto getPicture() {
        return picture;
    }

    public void setPicture(PicturePlayerDto picture) {
        this.picture = picture;
    }

    public TeamPLayerDto getTeam() {
        return team;
    }

    public void setTeam(TeamPLayerDto team) {
        this.team = team;
    }
}
