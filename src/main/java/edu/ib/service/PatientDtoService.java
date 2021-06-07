package edu.ib.service;

import edu.ib.object.patient.Patient;
import edu.ib.object.patient.PatientDto;
import edu.ib.repository.PatientDtoRepository;
import edu.ib.security.PasswordEncoderConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class PatientDtoService {

    private PatientDtoRepository patientDtoRepository;

    @Autowired
    public PatientDtoService(PatientDtoRepository patientDtoRepository) {
        this.patientDtoRepository = patientDtoRepository;
    }

    public PatientDto addPatient(PatientDto patient){
        patientDtoRepository.save(patient);
        return patient;
    }

    public Optional<PatientDto> getPatientById(Long id){
        return patientDtoRepository.findById(id);
    }

    public Iterable<PatientDto> getAllPatients(){
        return patientDtoRepository.findAll();
    }

    public void editPatient(Patient patient){
        PatientDto patientDto=patientDtoRepository.findById(patient.getPesel()).get();
        patientDto.setName(patient.getName());
        patientDto.setPhoneNumber(patient.getPhoneNumber());
        patientDto.setEmail(patient.getEmail());
        patientDto.setSurname(patient.getSurname());
        patientDto.setDateOfBirth(LocalDate.parse(patient.getDateOfBirth()));
        patientDtoRepository.save(patientDto);
    }

    public void changePassword(Long pesel,String newPassword){
        PatientDto patientDto=patientDtoRepository.findById(pesel).get();
        PasswordEncoderConfig c=new PasswordEncoderConfig();
        PasswordEncoder passwordEncoder=c.passwordEncoder();
        patientDto.setHashedPassword(passwordEncoder.encode(newPassword));
        patientDtoRepository.save(patientDto);
    }
}
