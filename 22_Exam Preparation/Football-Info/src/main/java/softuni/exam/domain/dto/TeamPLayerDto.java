package softuni.exam.domain.dto;

import com.google.gson.annotations.Expose;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class TeamPLayerDto {

    @Expose
    private String name;
    @Expose
    private PicturePlayerDto picture;

    @Size(min = 3, max = 20)
    @NotBlank
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PicturePlayerDto getPicture() {
        return picture;
    }

    public void setPicture(PicturePlayerDto picture) {
        this.picture = picture;
    }
}
