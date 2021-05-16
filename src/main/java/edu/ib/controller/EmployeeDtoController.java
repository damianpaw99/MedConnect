package edu.ib.controller;

import edu.ib.object.employee.Employee;
import edu.ib.object.employee.EmployeeDto;
import edu.ib.object.employee.EmployeeDtoBuilder;
import edu.ib.service.EmployeeDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class EmployeeDtoController {

    private EmployeeDtoService employeeDtoService;

    @Autowired
    public EmployeeDtoController(EmployeeDtoService employeeDtoService) {
        this.employeeDtoService = employeeDtoService;
    }

    @GetMapping("employee/admin/registration")
    public String getEmployeeRegistration(Model model){
        model.addAttribute("employee",new Employee());
        return "registration_form_staff";
    }


    @PostMapping("employee/admin/registration")
    public String createEmployee(@ModelAttribute Employee employee, Model model){
        EmployeeDtoBuilder builder=new EmployeeDtoBuilder();
        EmployeeDto employeeDto=builder.build(employee);
        employeeDtoService.addEmployee(employeeDto);
        return "redirect:/home";
    }
}
