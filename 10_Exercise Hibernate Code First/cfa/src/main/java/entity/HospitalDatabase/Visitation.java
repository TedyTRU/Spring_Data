package entity.HospitalDatabase;

import entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table (name = "visitations")
public class Visitation extends BaseEntity {

    private Date date;
    private String comments;
    private Patient patient;

    public Visitation() {
    }

    @Column
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Column(columnDefinition = "TEXT")
    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @ManyToOne
    @JoinColumn(name = "patient_id", referencedColumnName = "id")
    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}
