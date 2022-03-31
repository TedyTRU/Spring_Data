package softuni.exam.models.dto.seed.TicketsSeed;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class PassengerEmailDto {

    @XmlElement(name = "email")
    private String email;

    public PassengerEmailDto() {
    }

    @Email
    @Column(unique = true)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
