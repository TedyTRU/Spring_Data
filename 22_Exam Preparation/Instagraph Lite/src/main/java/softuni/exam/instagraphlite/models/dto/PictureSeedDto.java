package softuni.exam.instagraphlite.models.dto;

import com.google.gson.annotations.Expose;

import javax.persistence.Column;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class PictureSeedDto {

    @Expose
    private String path;
    @Expose
    private double size;

    public PictureSeedDto() {
    }

    @NotBlank
    @Column(unique = true)
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @DecimalMin(value = "500")
    @DecimalMax(value = "60000")
    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }
}
