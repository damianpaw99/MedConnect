package edu.ib.object;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private PatientDto patient;

    @ManyToOne
    private DoctorDto doctor;

    @ManyToOne
    private EmployeeDto employee;

    private LocalDateTime dateTime;

    public Appointment() {
    }

    public Appointment(PatientDto patient, DoctorDto doctor, EmployeeDto employee, LocalDateTime dateTime) {
        this.patient = patient;
        this.doctor = doctor;
        this.employee = employee;
        this.dateTime = dateTime;
    }

    public Appointment(Long id, PatientDto patient, DoctorDto doctor, EmployeeDto employee, LocalDateTime dateTime) {
        this.id = id;
        this.patient = patient;
        this.doctor = doctor;
        this.employee = employee;
        this.dateTime = dateTime;
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

    public DoctorDto getDoctor() {
        return doctor;
    }

    public void setDoctor(DoctorDto doctor) {
        this.doctor = doctor;
    }

    public EmployeeDto getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeDto employee) {
        this.employee = employee;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
