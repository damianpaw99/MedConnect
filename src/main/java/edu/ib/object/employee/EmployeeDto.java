package edu.ib.object.employee;

import edu.ib.object.Appointment;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity(name = "employees")
public class EmployeeDto {

    @Id
    private Long pesel;

    private String name;

    private String surname;

    @Column(name="birth_date")
    private LocalDate dateOfBirth;

    @Column(name="phone")
    private Integer phoneNumber;

    private String email;

    @Column(name="occupation")
    private String position;

    @Column(name="password")
    private String hashedPassword;

    @OneToMany
    private Set<Appointment> appointments;

    public EmployeeDto() {
    }

    public EmployeeDto(Long pesel, String name, String surname, LocalDate dateOfBirth, Integer phoneNumber, String email, String position, String hashedPassword) {
        this.pesel = pesel;
        this.name = name;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.position = position;
        this.hashedPassword = hashedPassword;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
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
}
