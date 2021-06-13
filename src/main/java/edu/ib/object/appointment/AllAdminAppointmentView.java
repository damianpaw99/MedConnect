package edu.ib.object.appointment;

import org.springframework.data.annotation.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Immutable
@Table(name="all_appointments_admin")
public class AllAdminAppointmentView {

    @Id
    private Long id;

    @Column(name = "date_godzina")
    private LocalDateTime dateTime;

    private String name;

    private String surname;

    @Column(name = "patient_pesel")
    private Long patientPesel;

    @Column(name = "doctor_pesel")
    private Long doctorPesel;

    public AllAdminAppointmentView() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Long getPatientPesel() {
        return patientPesel;
    }

    public void setPatientPesel(Long patientPesel) {
        this.patientPesel = patientPesel;
    }

    public Long getDoctorPesel() {
        return doctorPesel;
    }

    public void setDoctorPesel(Long doctorPesel) {
        this.doctorPesel = doctorPesel;
    }
}
