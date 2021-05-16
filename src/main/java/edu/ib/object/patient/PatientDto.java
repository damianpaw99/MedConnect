package edu.ib.object.patient;

import edu.ib.object.Appointment;
import edu.ib.object.Result;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.Set;

@Entity(name="patients")
public class PatientDto {

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

    @OneToMany(mappedBy="patient")
    private Set<Appointment> appointments;

    @OneToMany(mappedBy="patient")
    private Set<Result> results;


    public PatientDto() {
    }

    public PatientDto(Long pesel, String name, String surname, LocalDate dateOfBirth, Integer phoneNumber, String email, String hashedPassword) {
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

    public Set<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(Set<Appointment> appointments) {
        this.appointments = appointments;
    }

    public Set<Result> getResults() {
        return results;
    }

    public void setResults(Set<Result> results) {
        this.results = results;
    }
}
