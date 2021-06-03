package edu.ib.controller;

import edu.ib.object.doctor.Doctor;
import edu.ib.object.doctor.DoctorDtoBuilder;
import edu.ib.security.DataTokenReader;
import edu.ib.service.DoctorDtoService;
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
@RequestMapping("/doctor")
public class DoctorDtoController {

    private DoctorDtoService doctorDtoService;

    @Value("${jwt.login.token.key}")
    private String signingKey;

    @Autowired
    public DoctorDtoController(DoctorDtoService doctorDtoService) {
        this.doctorDtoService = doctorDtoService;
    }

    @GetMapping("/admin/registration")
    public String getDoctorRegistration(Model model, HttpServletRequest request){
        setRoleToModel(model,request);
        model.addAttribute("doctor",new Doctor());
        return "registration_form_doctor";
    }

    @PostMapping("/admin/registration")
    public String createDoctor(@ModelAttribute Doctor doctor,Model model){
        DoctorDtoBuilder builder=new DoctorDtoBuilder();
        doctorDtoService.addDoctor(builder.build(doctor));
        return "redirect:/home";
    }

    @GetMapping("/menu")
    public String getMenu(Model model, HttpServletRequest request){
        setRoleToModel(model,request);
        return "doctor";
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
