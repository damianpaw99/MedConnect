package edu.ib.service;

import edu.ib.object.Specialization;
import edu.ib.object.doctor.Doctor;
import edu.ib.object.doctor.DoctorDto;
import edu.ib.repository.DoctorDtoRepository;
import edu.ib.security.PasswordEncoderConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class DoctorDtoService {

    private DoctorDtoRepository doctorDtoRepository;

    @Autowired
    public DoctorDtoService(DoctorDtoRepository doctorDtoRepository) {
        this.doctorDtoRepository = doctorDtoRepository;
    }

    public void addDoctor(DoctorDto doctorDto){
        doctorDtoRepository.addDoctor(doctorDto.getPesel(),
                doctorDto.getName(),
                doctorDto.getSurname(),
                Date.valueOf(doctorDto.getDateOfBirth()),
                doctorDto.getPhoneNumber(),
                doctorDto.getEmail(),
                doctorDto.getHashedPassword());
        for(int i=0;i<doctorDto.getSpecializations().size();i++){
            doctorDtoRepository.addSpecToDoctor(doctorDto.getPesel(),((Specialization) doctorDto.getSpecializations().toArray()[i]).getName());
        }
    }

    public Optional<DoctorDto> getDoctorById(Long id){
        return doctorDtoRepository.findById(id);
    }

    public void editDoctor(Doctor doctor){
        DoctorDto doctorDto=doctorDtoRepository.findById(doctor.getPesel()).get();
        doctorDto.setPhoneNumber(doctor.getPhoneNumber());
        doctorDto.setEmail(doctor.getEmail());
        doctorDto.setDateOfBirth(LocalDate.parse(doctor.getDateOfBirth()));
        doctorDto.setSurname(doctor.getSurname());
        doctorDto.setName(doctor.getName());
        doctorDtoRepository.save(doctorDto);
    }

    public Iterable<DoctorDto> getAllDoctors() {
        return doctorDtoRepository.findAll();
    }

    public void changePassword(Long pesel, String newPassword){
        DoctorDto doctorDto=doctorDtoRepository.findById(pesel).get();
        PasswordEncoderConfig c=new PasswordEncoderConfig();
        PasswordEncoder passwordEncoder=c.passwordEncoder();
        doctorDto.setHashedPassword(passwordEncoder.encode(newPassword));
        doctorDtoRepository.save(doctorDto);
    }
}
