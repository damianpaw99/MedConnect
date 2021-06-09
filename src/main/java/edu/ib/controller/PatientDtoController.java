package edu.ib.controller;

import edu.ib.object.appointment.AllAppointmentView;
import edu.ib.object.appointment.Appointment;
import edu.ib.object.appointment.FreeAppointmentView;
import edu.ib.object.patient.Patient;
import edu.ib.object.patient.PatientDto;
import edu.ib.object.patient.PatientDtoBuilder;
import edu.ib.security.DataTokenReader;
import edu.ib.service.AppointmentService;
import edu.ib.service.PatientDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Controller
public class PatientDtoController {

    private PatientDtoService patientDtoService;
    private AppointmentService appointmentService;
    @Value("${jwt.login.token.key}")
    private String signingKey;

    @Autowired
    public PatientDtoController(PatientDtoService patientDtoService, AppointmentService appointmentService) {
        this.patientDtoService = patientDtoService;
        this.appointmentService = appointmentService;
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

    @PostMapping("/patient/admin/setAppointment/{id}")
    public String setAppointment(@PathVariable Long id, HttpServletRequest request) throws Exception {
        Appointment appointment = null;
        PatientDto patient = null;

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
        if (appointmentService.findAppointment(id).isPresent()){
            appointment = appointmentService.findAppointment(id).get();
            patient=patientDtoService.findPatient(pesel);
            appointment.setPatient(patient);
            appointmentService.deleteAppointment(id);
            appointmentService.addAppointment(appointment);
        }


        return "redirect:/patient/admin/freeAppointments";
    }

    @PostMapping("/patient/admin/cancelAppointment/{id}")
    public String cancelAppointment(@PathVariable Long id, HttpServletRequest request) throws Exception {
        Appointment appointment = null;

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
        if (appointmentService.findAppointment(id).isPresent()){
            appointment = appointmentService.findAppointment(id).get();
            if (LocalDateTime.now().isBefore(appointment.getDateTime().minusDays(1))){
                appointment.setPatient(null);
                appointmentService.deleteAppointment(id);
                appointmentService.addAppointment(appointment);
            }

        }

        return "redirect:/patient/admin/patientAppointments";
    }

    @GetMapping("/patient/menu")
    public String getMenu(Model model, HttpServletRequest request){
        setRoleToModel(model,request);
        return "patient";
    }

    @GetMapping("/patient/admin/freeAppointments")
    public String getAllAppointments(Model model, HttpServletRequest request){
        setRoleToModel(model,request);
        Iterable<FreeAppointmentView> freeAppointments=appointmentService.getFreeViewAppointments();
        model.addAttribute("appointmentsList",freeAppointments);
        return "freeAppointments";
    }

    @GetMapping("/patient/admin/patientAppointments")
    public String getPatientAppointments(Model model, HttpServletRequest request){
        setRoleToModel(model,request);
        ArrayList<AllAppointmentView> patientAppointments = new ArrayList<>();

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
                if (element.getPatientPesel().equals(finalPesel)){
                    patientAppointments.add(element);
                }
            }
        });
        model.addAttribute("appointmentsList",patientAppointments);
        return "patientAppointments";
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
