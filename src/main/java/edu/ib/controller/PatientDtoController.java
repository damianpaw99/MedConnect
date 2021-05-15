package edu.ib.controller;

import edu.ib.service.PatientDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PatientDtoController {

    private PatientDtoService patientDtoService;

    @Autowired
    public PatientDtoController(PatientDtoService patientDtoService) {
        this.patientDtoService = patientDtoService;
    }

    @GetMapping(value="/patient/registration/register")
    public void createUser(){

    }


}
