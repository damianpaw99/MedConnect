package edu.ib.service;

import edu.ib.object.patient.PatientDto;
import edu.ib.repository.PatientDtoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
