package edu.ib.object;

import edu.ib.object.doctor.DoctorDto;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Specialization {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "specializations")
    private Set<DoctorDto> doctor;

    public Specialization() {
    }

    public Specialization(String name) {
        this.name = name;
    }

    public Specialization(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<DoctorDto> getDoctor() {
        return doctor;
    }

    public void setDoctor(Set<DoctorDto> doctor) {
        this.doctor = doctor;
    }
}
