package edu.ib.controller;

import edu.ib.object.AllResultsView;
import edu.ib.object.Result;
import edu.ib.object.appointment.AllAppointmentView;
import edu.ib.object.appointment.Appointment;
import edu.ib.object.doctor.Doctor;
import edu.ib.object.doctor.DoctorDto;
import edu.ib.object.doctor.DoctorDtoBuilder;
import edu.ib.security.DataTokenReader;
import edu.ib.security.Logger;
import edu.ib.service.AppointmentService;
import edu.ib.service.DoctorDtoService;
import edu.ib.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Controller
@RequestMapping("/doctor")
public class DoctorDtoController {

    private DoctorDtoService doctorDtoService;
    private AppointmentService appointmentService;
    private ResultService resultService;

    @Value("${jwt.login.token.key}")
    private String signingKey;

    @Autowired
    public DoctorDtoController(DoctorDtoService doctorDtoService, AppointmentService appointmentService, ResultService resultService) {
        this.doctorDtoService = doctorDtoService;
        this.appointmentService=appointmentService;
        this.resultService=resultService;

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

    @GetMapping("/doctorAppointments")
    public String getDoctorAppointments(Model model, HttpServletRequest request){
        setRoleToModel(model,request);
        ArrayList<AllAppointmentView> doctorAppointments = new ArrayList<>();

        Cookie[] cookies = request.getCookies();
        Long pesel=null;
        if(cookies!=null){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    DataTokenReader reader=new DataTokenReader(signingKey);
                    pesel=reader.readPesel(cookie.getValue());
                    break;
                }
            }
        }

        Iterable<AllAppointmentView> allAppointments=appointmentService.getAllViewAppointments();
        Long finalPesel = pesel;
        allAppointments.forEach((element) -> {
            if (element.getPatientPesel() != null){
                if (element.getDoctorPesel().equals(finalPesel)){
                    doctorAppointments.add(element);
                }
            }
        });
        model.addAttribute("appointmentsList",doctorAppointments);
        model.addAttribute("now", LocalDateTime.now());
        return "doctorAppointments";
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

    @GetMapping("/doctor/editData")
    public String editData(Model model,HttpServletRequest request){
        setRoleToModel(model,request);
        Cookie[] cookies = request.getCookies();
        Long pesel=null;
        if(cookies!=null){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    DataTokenReader reader=new DataTokenReader(signingKey);
                    pesel = reader.readPesel(cookie.getValue());
                    break;
                }
            }
        }
        DoctorDto doctorDto=doctorDtoService.getDoctorById(pesel).get();
        Doctor doctor=new Doctor();
        doctor.setPesel(doctorDto.getPesel());
        doctor.setEmail(doctorDto.getEmail());
        doctor.setPhoneNumber(doctorDto.getPhoneNumber());
        doctor.setSurname(doctorDto.getSurname());
        doctor.setName(doctorDto.getName());
        doctor.setDateOfBirth(doctorDto.getDateOfBirth().toString());
        model.addAttribute("doctor",doctor);
        model.addAttribute("logger",new Logger());
        return "edit_doctor";
    }
    @PatchMapping("/doctor/editData")
    public String editData(@ModelAttribute Doctor doctor){
        doctorDtoService.editDoctor(doctor);
        return "redirect:/doctor/menu";
    }

    @GetMapping("/appointmentDetails/{id}")
    public String getAppointmentsDetails(@PathVariable Long id, Model model){
        Iterable<AllResultsView> results = resultService.getAllViewResults();
        Optional<AllAppointmentView> app = appointmentService.getAppointmentById(id);
        ArrayList<AllResultsView> finalResults = new ArrayList<>();

        results.forEach((element) -> {
            if (app.isPresent() && element.getPatientPesel()== app.get().getPatientPesel()){
                finalResults.add(element);
            }
        });

        model.addAttribute("resultsList", finalResults);


        return "patientResults";
    }

    @PutMapping("/doctor/editPassword")
    public String editPassword(@ModelAttribute Logger logger,HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        Long pesel=null;
        if(cookies!=null){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    DataTokenReader reader=new DataTokenReader(signingKey);
                    pesel = reader.readPesel(cookie.getValue());
                    break;
                }
            }
        }
        doctorDtoService.changePassword(pesel,logger.getPassword());
        return "redirect:/logout";
    }
}
