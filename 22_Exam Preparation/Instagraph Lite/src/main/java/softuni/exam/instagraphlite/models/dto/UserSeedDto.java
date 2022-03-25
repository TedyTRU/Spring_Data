package softuni.exam.instagraphlite.models.dto;

import com.google.gson.annotations.Expose;
import softuni.exam.instagraphlite.models.entity.Picture;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserSeedDto {

    @Expose
    String username;
    @Expose
    String password;
    @Expose
    String profilePicture;

    @NotBlank
    @Column(unique = true)
    @Size(min = 2, max = 18)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @NotBlank
    @Size(min = 4)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @NotBlank
    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}
