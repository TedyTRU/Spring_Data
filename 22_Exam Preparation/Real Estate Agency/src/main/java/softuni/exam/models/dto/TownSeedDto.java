package softuni.exam.models.dto;

import com.google.gson.annotations.Expose;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class TownSeedDto {

    @Expose
    private String townName;
    @Expose
    private Integer population;

    @Size(min = 2)
    @Column(unique = true)
    @NotBlank
    public String getTownName() {
        return townName;
    }

    public void setTownName(String townName) {
        this.townName = townName;
    }

    @Positive
    @NotNull
    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }
}
