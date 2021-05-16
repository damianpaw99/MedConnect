package edu.ib.controller;

import edu.ib.object.doctor.Doctor;
import edu.ib.object.doctor.DoctorDtoBuilder;
import edu.ib.service.DoctorDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/doctor")
public class DoctorDtoController {

    private DoctorDtoService doctorDtoService;

    @Autowired
    public DoctorDtoController(DoctorDtoService doctorDtoService) {
        this.doctorDtoService = doctorDtoService;
    }

    @GetMapping("/admin/registration")
    public String getDoctorRegistration(Model model){
        model.addAttribute("doctor",new Doctor());
        return "registration_form_doctor";
    }

    @PostMapping("/admin/registration")
    public String createDoctor(@ModelAttribute Doctor doctor,Model model){
        DoctorDtoBuilder builder=new DoctorDtoBuilder();
        doctorDtoService.addDoctor(builder.build(doctor));
        return "redirect:/home";
    }
}
