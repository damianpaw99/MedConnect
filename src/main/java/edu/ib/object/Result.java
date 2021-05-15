package edu.ib.object;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private PatientDto patient;

    private String type;

    private String photo;

    private LocalDateTime time;

    private String description;

    public Result() {
    }

    public Result(PatientDto patient, String type, String photo, LocalDateTime time, String description) {
        this.patient = patient;
        this.type = type;
        this.photo = photo;
        this.time = time;
        this.description = description;
    }

    public Result(Long id, PatientDto patient, String type, String photo, LocalDateTime time, String description) {
        this.id = id;
        this.patient = patient;
        this.type = type;
        this.photo = photo;
        this.time = time;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PatientDto getPatient() {
        return patient;
    }

    public void setPatient(PatientDto patient) {
        this.patient = patient;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
