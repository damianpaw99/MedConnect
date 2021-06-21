package edu.ib.object.patient;

import java.time.LocalDate;

public class Patient {

    private Long pesel;

    private String name;

    private String surname;

    private String dateOfBirth;

    private Integer phoneNumber;

    private String email;

    private String password;

    public Patient() {
    }

    public Patient(Long pesel, String name, String surname, String dateOfBirth, Integer phoneNumber, String email, String password) {
        this.pesel = pesel;
        this.name = name;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
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

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isCorrect() {
        if(name.equals("")){
            password="";
            return false;
        }
        if(surname.equals("")){
            password="";
            return false;
        }
        if(dateOfBirth == null){
            password="";
            return false;
        }
        if(password.equals("") || password.length()<5 || password.length()>10){
            password="";
            return false;
        }

        if(phoneNumber == null){
            password= "";
            return false;
        }
        if(pesel==null || pesel>99999999999L){
            password="";
            return false;
        }
        return true;

    }

    public boolean isEditionCorrect() {
        if(name.equals("")){
            password="";
            return false;
        }
        if(surname.equals("")){
            password="";
            return false;
        }
        if(dateOfBirth == null){
            password="";
            return false;
        }

        if(phoneNumber == null){
            password= "";
            return false;
        }
        if(pesel==null || pesel>99999999999L){
            password="";
            return false;
        }
        return true;

    }

}
