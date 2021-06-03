package edu.ib.object.doctor;

import edu.ib.object.appointment.Appointment;
import edu.ib.object.Specialization;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity(name="doctors")
public class DoctorDto {

    @Id
    private Long pesel;

    private String name;

    private String surname;

    @Column(name="birth_date")
    private LocalDate dateOfBirth;

    @Column(name="phone")
    private Integer phoneNumber;

    private String email;

    @Column(name="password")
    private String hashedPassword;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name="doctors_specs",
            joinColumns = {@JoinColumn(name="pesel")},
            inverseJoinColumns = {@JoinColumn(name="id")})
    private Set<Specialization> specializations;

    @OneToMany(mappedBy = "doctor")
    private Set<Appointment> appointments;

    public DoctorDto() {
    }

    public DoctorDto(Long pesel, String name, String surname, LocalDate dateOfBirth, Integer phoneNumber, String email, String hashedPassword) {
        this.pesel = pesel;
        this.name = name;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.hashedPassword = hashedPassword;
    }

    public Long getPesel() {
        return pesel;
    }

    public void setPesel(Long pesel) {
        this.pesel = pesel;
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

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Integer getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Integer phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public Set<Specialization> getSpecializations() {
        return specializations;
    }

    public void setSpecializations(Set<Specialization> specializations) {
        this.specializations = specializations;
    }

    public Set<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(Set<Appointment> appointments) {
        this.appointments = appointments;
    }
}
