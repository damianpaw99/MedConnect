package edu.ib.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DoctorDtoService {

    private DoctorDtoService doctorDtoService;

    @Autowired
    public DoctorDtoService(DoctorDtoService doctorDtoService) {
        this.doctorDtoService = doctorDtoService;
    }
}
