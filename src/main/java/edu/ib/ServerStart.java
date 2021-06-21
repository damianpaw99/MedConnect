package edu.ib;

import edu.ib.object.employee.EmployeeDto;
import edu.ib.security.PasswordEncoderConfig;
import edu.ib.service.EmployeeDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ServerStart {

    private EmployeeDtoService employeeDtoService;

    @Autowired
    public ServerStart(EmployeeDtoService employeeDtoService) {
        this.employeeDtoService = employeeDtoService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void createFirstAdmin(){

        Iterable <EmployeeDto> all= employeeDtoService.getAllEmployees();
        if(all.spliterator().estimateSize()==0) {
            EmployeeDto employee = new EmployeeDto();
            employee.setName("MASTER");
            employee.setSurname("ADMIN");
            employee.setDateOfBirth(LocalDate.now());
            employee.setPesel(11111111111L);
            employee.setEmail("");
            employee.setPosition("ADMIN");
            employee.setPhoneNumber(0);
            PasswordEncoderConfig c = new PasswordEncoderConfig();
            PasswordEncoder passwordEncoder = c.passwordEncoder();
            employee.setHashedPassword(passwordEncoder.encode("password"));
            employeeDtoService.addEmployee(employee);
        }
    }


}
