package softuni.exam.models.dto.seed.TicketsSeed;

import javax.persistence.Column;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class PlaneNumberDto {

    @XmlElement(name = "register-number")
    private String registerNumber;

    public PlaneNumberDto() {
    }

    @Size(min = 5)
    @Column(unique = true)
    public String getRegisterNumber() {
        return registerNumber;
    }

    public void setRegisterNumber(String registerNumber) {
        this.registerNumber = registerNumber;
    }
}
