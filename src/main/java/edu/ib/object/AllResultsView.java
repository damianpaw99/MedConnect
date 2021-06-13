package edu.ib.object;

import org.springframework.data.annotation.Immutable;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Immutable
@Table(name="all_results")
public class AllResultsView {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "insert_date")
    private LocalDateTime time;

    private String type;

    private String photos;

    private String description;

    @Column(name = "patient_pesel")
    private long patientPesel;

    public long getPatientPesel() {
        return patientPesel;
    }

    public void setPatientPesel(long patientPesel) {
        this.patientPesel = patientPesel;
    }


    public AllResultsView(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
