package edu.ib.object.patient;

import edu.ib.security.PasswordEncoderConfig;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

public class PatientDtoBuilder {

    public PatientDto build(Patient patient){
        PatientDto pdto=new PatientDto();
        pdto.setName(patient.getName());
        pdto.setSurname(patient.getSurname());
        pdto.setPesel(patient.getPesel());
        pdto.setDateOfBirth(LocalDate.parse(patient.getDateOfBirth()));
        pdto.setEmail(patient.getEmail());
        pdto.setPhoneNumber(patient.getPhoneNumber());
        PasswordEncoderConfig passwordEncoderConfig=new PasswordEncoderConfig();
        PasswordEncoder passwordEncoder=passwordEncoderConfig.passwordEncoder();
        pdto.setHashedPassword(passwordEncoder.encode(patient.getPassword()));
        return pdto;
    }
}
