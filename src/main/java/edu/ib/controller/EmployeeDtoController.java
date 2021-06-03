package edu.ib.controller;

import edu.ib.object.appointment.AllAppointmentView;
import edu.ib.object.employee.Employee;
import edu.ib.object.employee.EmployeeDto;
import edu.ib.object.employee.EmployeeDtoBuilder;
import edu.ib.otherModels.Timetable;
import edu.ib.security.DataTokenReader;
import edu.ib.service.AppointmentService;
import edu.ib.service.EmployeeDtoService;
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
    @Value("${jwt.login.token.key}")
    private String signingKey;

    @Autowired
    public EmployeeDtoController(EmployeeDtoService employeeDtoService, AppointmentService appointmentService) {
        this.employeeDtoService = employeeDtoService;
        this.appointmentService = appointmentService;
    }

    @GetMapping("/employee/admin/registration")
    public String getEmployeeRegistration(Model model, HttpServletRequest request){
        setRoleToModel(model, request);
        model.addAttribute("employee",new Employee());
        return "registration_form_staff";
    }


    @PostMapping("/employee/admin/registration")
    public String createEmployee(@ModelAttribute Employee employee){
        EmployeeDtoBuilder builder=new EmployeeDtoBuilder();
        EmployeeDto employeeDto=builder.build(employee);
        employeeDtoService.addEmployee(employeeDto);
        return "redirect:/home";
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
        model.addAttribute("timetable",new Timetable());
        appointmentService.addAppointmentsFromTimetable(timetable,employeePesel);
        return "timetable";
    }

    @GetMapping("/employee/admin/allAppointments")
    public String getAllAppointments(Model model, HttpServletRequest request){
        setRoleToModel(model,request);
        Iterable<AllAppointmentView> allAppointments=appointmentService.getAllViewAppointments();
        model.addAttribute("appointmentsList",allAppointments);
        return "allAppointments";
    }

    @DeleteMapping("/employee/admin/deleteAppointment/{id}")
    public String deleteAppointment(@PathVariable Long id){
        appointmentService.deleteAppointment(id);
        return "redirect:/employee/admin/allAppointments";
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
