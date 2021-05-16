package edu.ib.service;

import edu.ib.object.Specialization;
import edu.ib.object.doctor.DoctorDto;
import edu.ib.repository.DoctorDtoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;

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


    /*
    public void addDoctor(DoctorDto doctorDto){
        doctorDtoRepository.save(doctorDto);
    }

     */
}
