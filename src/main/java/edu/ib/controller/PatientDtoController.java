package edu.ib.controller;

import edu.ib.object.AllResultsView;
import edu.ib.object.Result;
import edu.ib.object.appointment.AllAppointmentView;
import edu.ib.object.appointment.Appointment;
import edu.ib.object.appointment.FreeAppointmentView;
import edu.ib.object.patient.Patient;
import edu.ib.object.patient.PatientDto;
import edu.ib.object.patient.PatientDtoBuilder;
import edu.ib.otherModels.ResultModel;
import edu.ib.otherModels.Timetable;
import edu.ib.security.DataTokenReader;
import edu.ib.service.AppointmentService;
import edu.ib.security.Logger;
import edu.ib.service.PatientDtoService;
import edu.ib.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Controller
public class PatientDtoController {

    private PatientDtoService patientDtoService;
    private AppointmentService appointmentService;
    private ResultService resultService;
    @Value("${jwt.login.token.key}")
    private String signingKey;

    @Autowired
    public PatientDtoController(PatientDtoService patientDtoService, AppointmentService appointmentService, ResultService resultService) {
        this.patientDtoService = patientDtoService;
        this.appointmentService = appointmentService;
        this.resultService = resultService;
    }

    @GetMapping("/patient/registration")
    public String getPatientRegistration(Model model, HttpServletRequest request){
        setRoleToModel(model,request);
        model.addAttribute("patient",new Patient());
        return "registration_form_patient";
    }

    @PostMapping("/patient/registration")
    public String createPatient(@ModelAttribute Patient patient, Model model, HttpServletRequest request){
        if(patient.isCorrect()) {
            PatientDtoBuilder builder = new PatientDtoBuilder();
            PatientDto patientDto = builder.build(patient);
            patientDtoService.addPatient(patientDto);
            return "redirect:/login";
        } else {
            setRoleToModel(model,request);
            model.addAttribute("patient",patient);
            return "registration_form_patient";
        }
    }

    @GetMapping("/patient/freeAppointments/filtered")
    public String filterData(Model model, HttpServletRequest request, String keyword){
        setRoleToModel(model,request);
        Iterable<FreeAppointmentView> filteredFreeAppointments = appointmentService.getFreeViewAppointmentsByKeyword(keyword);
        model.addAttribute("appointmentsList",filteredFreeAppointments);
        return "freeAppointments";

    }

    @PostMapping("/patient/setAppointment/{id}")
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
            appointmentService.addAppointment(appointment); //dodanie wizyty posiadającej takie samo id jak w bazie danych ją podmienia
        }
        return "redirect:/patient/freeAppointments";
    }

    @PostMapping("/patient/cancelAppointment/{id}")
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

        return "redirect:/patient/patientAppointments";
    }

    @GetMapping("/patient/menu")
    public String getMenu(Model model, HttpServletRequest request){
        setRoleToModel(model,request);
        return "patient";
    }

    @GetMapping("/patient/freeAppointments")
    public String getAllAppointments(Model model, HttpServletRequest request){
        setRoleToModel(model,request);
        Iterable<FreeAppointmentView> freeAppointments=appointmentService.getFreeViewAppointments();
        model.addAttribute("appointmentsList",freeAppointments);
        return "freeAppointments";
    }

    @GetMapping("/patient/viewResults")
    public String getAllResults(Model model, HttpServletRequest request){
        setRoleToModel(model,request);
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
        Iterable<AllResultsView> allResultsViews=resultService.getPatientResults(pesel);
        model.addAttribute("resultsList",allResultsViews);
        return "patientResults";
    }

    @GetMapping("/patient/patientAppointments")
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

    @GetMapping("/patient/editData")
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
        PatientDto patientDto=patientDtoService.getPatientById(pesel).get();
        Patient patient=new Patient();
        patient.setPesel(patientDto.getPesel());
        patient.setEmail(patientDto.getEmail());
        patient.setPhoneNumber(patientDto.getPhoneNumber());
        patient.setSurname(patientDto.getSurname());
        patient.setName(patientDto.getName());
        patient.setDateOfBirth(patientDto.getDateOfBirth().toString());
        model.addAttribute("patient",patient);
        model.addAttribute("logger",new Logger());
        return "edit_patient";
    }
    @PatchMapping("/patient/editData")
    public String editData(@ModelAttribute Patient patient, Model model,HttpServletRequest request){
        if(patient.isEditionCorrect()){
            patientDtoService.editPatient(patient);
            return "redirect:/patient/menu";
        } else{
            setRoleToModel(model,request);
            model.addAttribute("patient",patient);
            model.addAttribute("logger",new Logger());
            return "edit_patient";
        }

    }
    @PutMapping("/patient/changePassword")
    public String editPassword(@ModelAttribute Logger logger, Model model,HttpServletRequest request){
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
        if(logger.isPasswordCorrect()){
            patientDtoService.changePassword(pesel,logger.getPassword());
            return "redirect:/logouts";
        } else {
            setRoleToModel(model,request);
            PatientDto patientDto=patientDtoService.getPatientById(pesel).get();
            Patient patient=new Patient();
            patient.setPesel(patientDto.getPesel());
            patient.setEmail(patientDto.getEmail());
            patient.setPhoneNumber(patientDto.getPhoneNumber());
            patient.setSurname(patientDto.getSurname());
            patient.setName(patientDto.getName());
            patient.setDateOfBirth(patientDto.getDateOfBirth().toString());
            model.addAttribute("patient",patient);
            model.addAttribute("logger",new Logger());
            return "edit_patient";
        }
    }

    @GetMapping("/patient/addResult")
    public String getAddResultForm(Model model, HttpServletRequest request){
        setRoleToModel(model,request);
        model.addAttribute("result",new ResultModel());
        return "add_result_patient";
    }

    @PostMapping(value="/patient/addResult", consumes = {"multipart/form-data"})
    public String addResult(@ModelAttribute ResultModel result, @RequestParam("file") MultipartFile file, HttpServletRequest request, Model model){
        Cookie [] cookies=request.getCookies();
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
        if(result.isCorrect()) {
            Result resultDto = new Result();
            resultDto.setPatient(patientDtoService.findPatient(pesel));
            resultDto.setTime(LocalDateTime.now());
            resultDto.setType(result.getType());
            resultDto.setDescription(result.getDescription());
            try {

                String uploadDir = "/uploads/";
                String realPath = request.getServletContext().getRealPath(uploadDir);
                //String realPath = "./photos";
                String newFileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")) + ".png";
                File transferFile = new File(realPath + "/" + file.getOriginalFilename());
                //file.transferTo(transferFile);
                File save = new File("./src/main/resources/photos/" + file.getOriginalFilename());
                if (!save.getName().endsWith(".png")) return "redirect:/error";
                file.transferTo(save);
                File rename = new File("./src/main/resources/photos/" + newFileName);
                save.renameTo(rename);
                resultDto.setPhoto(newFileName);
            } catch (Exception e) {

                e.printStackTrace();
                return "redirect:/error";
            }
            resultService.addResult(resultDto);
            return "redirect:/patient/menu";
        } else {
            setRoleToModel(model, request);
            model.addAttribute("result",result);
            return "add_result_patient";
        }
    }
}
