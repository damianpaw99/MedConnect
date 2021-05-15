package edu.ib.service;

import edu.ib.repository.DoctorDtoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DoctorDtoService {

    private DoctorDtoRepository doctorDtoRepository;

    @Autowired
    public DoctorDtoService(DoctorDtoRepository doctorDtoRepository) {
        this.doctorDtoRepository = doctorDtoRepository;
    }
}
