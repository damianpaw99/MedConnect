package edu.ib.object.employee;

import edu.ib.security.PasswordEncoderConfig;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

public class EmployeeDtoBuilder {

    public EmployeeDto build(Employee employee){
        EmployeeDto employeeDto=new EmployeeDto();

        employeeDto.setPesel(employee.getPesel());
        employeeDto.setName(employee.getName());
        employeeDto.setSurname(employee.getSurname());
        employeeDto.setDateOfBirth(LocalDate.parse(employee.getDateOfBirth()));
        employeeDto.setEmail(employee.getEmail());
        employeeDto.setPhoneNumber(employee.getPhoneNumber());
        employeeDto.setPosition(employee.getPosition().toUpperCase());

        PasswordEncoderConfig passwordEncoderConfig=new PasswordEncoderConfig();
        PasswordEncoder passwordEncoder= passwordEncoderConfig.passwordEncoder();
        employeeDto.setHashedPassword(passwordEncoder.encode(employee.getPassword()));

        return employeeDto;
    }
}
