package edu.ib.controller;

import edu.ib.object.patient.Patient;
import edu.ib.object.patient.PatientDto;
import edu.ib.object.patient.PatientDtoBuilder;
import edu.ib.security.DataTokenReader;
import edu.ib.service.PatientDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class PatientDtoController {

    private PatientDtoService patientDtoService;

    @Value("${jwt.login.token.key}")
    private String signingKey;

    @Autowired
    public PatientDtoController(PatientDtoService patientDtoService) {
        this.patientDtoService = patientDtoService;
    }

    @GetMapping("/patient/registration")
    public String getPatientRegistration(Model model, HttpServletRequest request){
        setRoleToModel(model,request);
        model.addAttribute("patient",new Patient());
        return "registration_form_patient";
    }

    @PostMapping("/patient/registration")
    public String createPatient(@ModelAttribute Patient patient){
        PatientDtoBuilder builder=new PatientDtoBuilder();
        PatientDto patientDto=builder.build(patient);
        patientDtoService.addPatient(patientDto);
        return "redirect:/home";
    }

    @GetMapping("/patient/menu")
    public String getMenu(Model model, HttpServletRequest request){
        setRoleToModel(model,request);
        return "patient";
    }

    private void setRoleToModel(Model model, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String role=null;
        if(cookies!=null){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    DataTokenReader reader=new DataTokenReader(signingKey);
                    role=reader.readRole(cookie.getValue());
                    break;
                }
            }
        }
        model.addAttribute("role",role);
    }
}
