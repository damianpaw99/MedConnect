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

    @GetMapping("/doctor/admin/registration")
    public String getDoctorRegistration(Model model, HttpServletRequest request){
        setRoleToModel(model,request);
        model.addAttribute("doctor",new Doctor());
        return "registration_form_doctor";
    }

    @PostMapping("/doctor/admin/registration")
    public String createDoctor(@ModelAttribute Doctor doctor,Model model, HttpServletRequest request){
        DoctorDtoBuilder builder=new DoctorDtoBuilder();
        if(doctor.isCorrect()) {
            doctorDtoService.addDoctor(builder.build(doctor));
            return "redirect:/home";
        } else {
            model.addAttribute("doctor",doctor);
            setRoleToModel(model,request);
            return "registration_form_doctor";
        }
    }

    @GetMapping("/doctor/menu")
    public String getMenu(Model model, HttpServletRequest request){
        setRoleToModel(model,request);
        return "doctor";
    }

    @GetMapping("/doctor/doctorAppointments")
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
    public String editData(@ModelAttribute Doctor doctor, Model model,HttpServletRequest request){
        if(doctor.isEditionCorrect()) {
            doctorDtoService.editDoctor(doctor);
            return "redirect:/doctor/menu";
        } else {
            setRoleToModel(model, request);
            model.addAttribute("doctor",doctor);
            model.addAttribute("logger",new Logger());
            return "edit_doctor";
        }
    }

    @GetMapping("/doctor/appointmentDetails/{id}")
    public String getAppointmentsDetails(@PathVariable Long id, Model model, HttpServletRequest request){
        setRoleToModel(model,request);
        Optional<AllAppointmentView> app = appointmentService.getAppointmentById(id);
        Iterable<AllResultsView> results=null;
        if(app.isPresent()) {
            results = resultService.getPatientResults(app.get().getPatientPesel());
        }


        model.addAttribute("resultsList", results);


        return "patientResults";
    }

    @PutMapping("/doctor/changePassword")
    public String editPassword(@ModelAttribute Logger logger, HttpServletRequest request, Model model){
        if(logger.isPasswordCorrect()) {
            Cookie[] cookies = request.getCookies();
            Long pesel = null;
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("token")) {
                        DataTokenReader reader = new DataTokenReader(signingKey);
                        pesel = reader.readPesel(cookie.getValue());
                        break;
                    }
                }
            }
            doctorDtoService.changePassword(pesel, logger.getPassword());
            return "redirect:/logouts";
        } else {
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
    }
}
