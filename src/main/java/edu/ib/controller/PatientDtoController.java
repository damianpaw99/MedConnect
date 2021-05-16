package edu.ib.controller;

import edu.ib.object.patient.Patient;
import edu.ib.object.patient.PatientDto;
import edu.ib.object.patient.PatientDtoBuilder;
import edu.ib.service.PatientDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PatientDtoController {

    private PatientDtoService patientDtoService;

    @Autowired
    public PatientDtoController(PatientDtoService patientDtoService) {
        this.patientDtoService = patientDtoService;
    }

    @GetMapping("/patient/registration")
    public String getPatientRegistration(Model model){
        model.addAttribute("patient",new Patient());
        return "registration_form_patient";
    }

    @PostMapping("/patient/registration")
    public String createPatient(@ModelAttribute Patient patient, Model model){
        PatientDtoBuilder builder=new PatientDtoBuilder();
        PatientDto patientDto=builder.build(patient);
        patientDtoService.addPatient(patientDto);

        return "redirect:/home";
    }



}
