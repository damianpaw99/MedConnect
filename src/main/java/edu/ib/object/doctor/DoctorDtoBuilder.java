package edu.ib.object.doctor;

import edu.ib.object.Specialization;
import edu.ib.security.PasswordEncoderConfig;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public class DoctorDtoBuilder {

    public DoctorDto build(Doctor doctor){
        DoctorDto doctorDto=new DoctorDto();
        doctorDto.setName(doctor.getName());
        doctorDto.setSurname(doctor.getSurname());
        doctorDto.setDateOfBirth(LocalDate.parse(doctor.getDateOfBirth()));
        doctorDto.setEmail(doctor.getEmail());
        doctorDto.setPesel(doctor.getPesel());
        doctorDto.setPhoneNumber(doctor.getPhoneNumber());
        Set<Specialization> specSet= new java.util.HashSet<>(Collections.emptySet());
        String [] specArray=doctor.getSpecializations().split(",");

        for(int i=0;i<specArray.length;i++){
            specSet.add(new Specialization(specArray[i]));
        }

        doctorDto.setSpecializations(specSet);
        PasswordEncoderConfig config=new PasswordEncoderConfig();
        PasswordEncoder encoder=config.passwordEncoder();
        doctorDto.setHashedPassword(encoder.encode(doctor.getPassword()));
        return doctorDto;
    }
}
