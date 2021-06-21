package edu.ib.controller;

import edu.ib.object.appointment.AllAdminAppointmentView;
import edu.ib.object.appointment.AllAppointmentView;
import edu.ib.object.doctor.Doctor;
import edu.ib.object.doctor.DoctorDto;
import edu.ib.object.employee.Employee;
import edu.ib.object.employee.EmployeeDto;
import edu.ib.object.employee.EmployeeDtoBuilder;
import edu.ib.object.patient.Patient;
import edu.ib.object.patient.PatientDto;
import edu.ib.otherModels.Timetable;
import edu.ib.security.DataTokenReader;
import edu.ib.security.Logger;
import edu.ib.service.AppointmentService;
import edu.ib.service.DoctorDtoService;
import edu.ib.service.EmployeeDtoService;
import edu.ib.service.PatientDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class EmployeeDtoController {

    private EmployeeDtoService employeeDtoService;
    private AppointmentService appointmentService;
    private PatientDtoService patientDtoService;
    private DoctorDtoService doctorDtoService;
    @Value("${jwt.login.token.key}")
    private String signingKey;

    @Autowired
    public EmployeeDtoController(EmployeeDtoService employeeDtoService, AppointmentService appointmentService, PatientDtoService patientDtoService, DoctorDtoService doctorDtoService) {
        this.employeeDtoService = employeeDtoService;
        this.appointmentService = appointmentService;
        this.patientDtoService = patientDtoService;
        this.doctorDtoService = doctorDtoService;
    }

    @GetMapping("/employee/admin/registration")
    public String getEmployeeRegistration(Model model, HttpServletRequest request){
        setRoleToModel(model, request);
        model.addAttribute("employee",new Employee());
        return "registration_form_staff";
    }


    @PostMapping("/employee/admin/registration")
    public String createEmployee(@ModelAttribute Employee employee,Model model,HttpServletRequest request){
        if(employee.isCorrect()) {
            EmployeeDtoBuilder builder = new EmployeeDtoBuilder();
            EmployeeDto employeeDto = builder.build(employee);
            employeeDtoService.addEmployee(employeeDto);
            return "redirect:/home";
        } else {
            setRoleToModel(model,request);
            model.addAttribute("employee",employee);
            return "registration_form_staff";
        }
    }

    @GetMapping("/employee/menu")
    public String getMenu(Model model, HttpServletRequest request){
        setRoleToModel(model, request);
        return "employee";
    }



    @GetMapping("/employee/admin/createAppointments")
    public String getTimetableForm(Model model, HttpServletRequest request){
        setRoleToModel(model, request);
        model.addAttribute("timetable",new Timetable());
        return "timetable";
    }
    @PostMapping("/employee/admin/createAppointments")
    public String createTimetable(@ModelAttribute Timetable timetable, Model model, HttpServletRequest request) throws Exception {
        Cookie[] cookies = request.getCookies();
        Long employeePesel=null;
        String role=null;
        if(cookies!=null){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    DataTokenReader reader=new DataTokenReader(signingKey);
                    employeePesel = reader.readPesel(cookie.getValue());
                    role=reader.readRole(cookie.getValue());
                    break;
                }
            }
        }
        model.addAttribute("role",role);
        if(timetable.isCorrect()) {
            model.addAttribute("timetable", new Timetable());
            appointmentService.addAppointmentsFromTimetable(timetable, employeePesel);
        } else {
            model.addAttribute("timetable",timetable);
        }
        return "timetable";
    }

    @GetMapping("/employee/admin/allAppointments")
    public String getAllAppointments(Model model, HttpServletRequest request){
        setRoleToModel(model,request);
        Iterable<AllAdminAppointmentView> allAppointments=appointmentService.getAllViewAdminAppointments();
        model.addAttribute("appointmentsList",allAppointments);
        return "allAppointments";
    }

    @DeleteMapping("/employee/admin/deleteAppointment/{id}")
    public String deleteAppointment(@PathVariable Long id){
        appointmentService.deleteAppointment(id);
        return "redirect:/employee/admin/allAppointments";
    }

    @GetMapping("/employee/admin/allPatients")
    public String getAllPatients(Model model, HttpServletRequest request){
        setRoleToModel(model,request);
        model.addAttribute("patientList",patientDtoService.getAllPatients());
        return "all_patients";
    }

    @GetMapping("/employee/admin/editPatient/{id}")
    public String getPatient(@PathVariable Long id, Model model, HttpServletRequest request) throws Exception {
        setRoleToModel(model,request);
        if(patientDtoService.getPatientById(id).isEmpty()) throw new Exception();
        PatientDto patientDto=patientDtoService.getPatientById(id).get();
        Patient patient=new Patient();
        patient.setPesel(patientDto.getPesel());
        patient.setDateOfBirth(patientDto.getDateOfBirth().toString());
        patient.setName(patientDto.getName());
        patient.setSurname(patientDto.getSurname());
        patient.setPhoneNumber(patientDto.getPhoneNumber());
        patient.setEmail(patientDto.getEmail());
        model.addAttribute("patient",patient);
        return "edit_patient_admin";
    }

    @PatchMapping("/employee/admin/editPatient")
    public String editPatient(@ModelAttribute Patient patient, Model model,HttpServletRequest request){
        if(patient.isEditionCorrect()) {
            patientDtoService.editPatient(patient);
            return "redirect:/employee/admin/allPatients";
        } else {
            setRoleToModel(model,request);
            model.addAttribute("patient",patient);
            return "edit_patient_admin";
        }
    }


    @GetMapping("/employee/admin/allEmployees")
    public String getAllEmployees(Model model, HttpServletRequest request){
        setRoleToModel(model, request);
        model.addAttribute("employeeList",employeeDtoService.getAllEmployees());
        return "all_employees";
    }

    @PatchMapping("/employee/admin/editEmployee")
    public String editEmployee(@ModelAttribute Employee employee, Model model,HttpServletRequest request){
        if(employee.isEditionCorrect()) {
            employeeDtoService.editEmployee(employee);
            return "redirect:/employee/admin/allEmployees";
        } else {
            setRoleToModel(model, request);
            model.addAttribute("employee",employee);
            return "edit_employee_admin";
        }

    }
    @GetMapping("/employee/admin/editEmployee/{id}")
    public String getEmployee(@PathVariable Long id, Model model, HttpServletRequest request) throws Exception {
        setRoleToModel(model,request);
        if(employeeDtoService.getEmployeeById(id).isEmpty()) throw new Exception();
        EmployeeDto employeeDto=employeeDtoService.getEmployeeById(id).get();
        Employee employee=new Employee();
        employee.setPesel(employeeDto.getPesel());
        employee.setDateOfBirth(employeeDto.getDateOfBirth().toString());
        employee.setName(employeeDto.getName());
        employee.setSurname(employeeDto.getSurname());
        employee.setPhoneNumber(employeeDto.getPhoneNumber());
        employee.setEmail(employeeDto.getEmail());
        employee.setPosition(employeeDto.getPosition().toUpperCase());
        model.addAttribute("employee",employee);
        return "edit_employee_admin";
    }

    @GetMapping("/employee/admin/allDoctors")
    public String getAllDoctors(Model model, HttpServletRequest request){
        setRoleToModel(model, request);
        model.addAttribute("doctorList",doctorDtoService.getAllDoctors());
        return "all_doctors";
    }

    @PatchMapping("/employee/admin/editDoctor")
    public String editDoctor(@ModelAttribute Doctor doctor, Model model,HttpServletRequest request){
        if(doctor.isEditionCorrect()) {
            doctorDtoService.editDoctor(doctor);
            return "redirect:/employee/admin/allDoctors";
        } else {
            setRoleToModel(model,request);
            model.addAttribute("doctor",doctor);
            return "edit_doctor_admin";
        }
    }

    @GetMapping("/employee/admin/editDoctor/{id}")
    public String getDoctor(@PathVariable Long id, Model model, HttpServletRequest request) throws Exception {
        setRoleToModel(model,request);
        if(doctorDtoService.getDoctorById(id).isEmpty()) throw new Exception();
        DoctorDto doctorDto=doctorDtoService.getDoctorById(id).get();
        Doctor doctor=new Doctor();
        doctor.setPesel(doctorDto.getPesel());
        doctor.setDateOfBirth(doctorDto.getDateOfBirth().toString());
        doctor.setName(doctorDto.getName());
        doctor.setSurname(doctorDto.getSurname());
        doctor.setPhoneNumber(doctorDto.getPhoneNumber());
        doctor.setEmail(doctorDto.getEmail());
        model.addAttribute("doctor",doctor);
        return "edit_doctor_admin";
    }

    @GetMapping("/employee/editData")
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
        EmployeeDto employeeDto=employeeDtoService.getEmployeeById(pesel).get();
        Employee employee=new Employee();
        employee.setPesel(employeeDto.getPesel());
        employee.setEmail(employeeDto.getEmail());
        employee.setPhoneNumber(employeeDto.getPhoneNumber());
        employee.setSurname(employeeDto.getSurname());
        employee.setName(employeeDto.getName());
        employee.setDateOfBirth(employeeDto.getDateOfBirth().toString());
        employee.setPosition(employeeDto.getPosition());
        model.addAttribute("employee",employee);
        model.addAttribute("logger",new Logger());
        return "edit_employee";
    }
    @PatchMapping("/employee/editData")
    public String editData(@ModelAttribute Employee employee, Model model, HttpServletRequest request){
        if(employee.isEditionCorrect()) {
            employeeDtoService.editEmployee(employee);
            return "redirect:/employee/menu";
        } else {
            setRoleToModel(model,request);
            model.addAttribute("employee",employee);
            model.addAttribute("logger",new Logger());
            return "edit_employee";
        }
    }
    @PutMapping("/employee/editPassword")
    public String editPassword(@ModelAttribute Logger logger,HttpServletRequest request, Model model){
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
            employeeDtoService.changePassword(pesel, logger.getPassword());
            return "redirect:/logouts";
        } else {
            setRoleToModel(model,request);
            model.addAttribute("logger",new Logger());
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
            EmployeeDto employeeDto=employeeDtoService.getEmployeeById(pesel).get();
            Employee employee=new Employee();
            employee.setPesel(employeeDto.getPesel());
            employee.setEmail(employeeDto.getEmail());
            employee.setPhoneNumber(employeeDto.getPhoneNumber());
            employee.setSurname(employeeDto.getSurname());
            employee.setName(employeeDto.getName());
            employee.setDateOfBirth(employeeDto.getDateOfBirth().toString());
            employee.setPosition(employeeDto.getPosition());
            model.addAttribute("employee",employee);
            return "edit_employee";
        }
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
