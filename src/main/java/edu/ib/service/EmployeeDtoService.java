package edu.ib.service;

import edu.ib.object.employee.Employee;
import edu.ib.object.employee.EmployeeDto;
import edu.ib.repository.EmployeeDtoRepository;
import edu.ib.security.PasswordEncoderConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class EmployeeDtoService {

    private EmployeeDtoRepository employeeDtoRepository;

    @Autowired
    public EmployeeDtoService(EmployeeDtoRepository employeeDtoRepository) {
        this.employeeDtoRepository = employeeDtoRepository;
    }

    public void addEmployee(EmployeeDto employeeDto){
        employeeDtoRepository.save(employeeDto);
    }

    public Iterable<EmployeeDto> getAllEmployees(){
        return employeeDtoRepository.findAll();
    }

    public Optional<EmployeeDto> getEmployeeById(Long pesel){
        return employeeDtoRepository.findById(pesel);
    }

    public void editEmployee(Employee employee){
        EmployeeDto employeeDto=employeeDtoRepository.findById(employee.getPesel()).get();
        employeeDto.setPosition(employee.getPosition());
        employeeDto.setPhoneNumber(employee.getPhoneNumber());
        employeeDto.setEmail(employee.getEmail());
        employeeDto.setDateOfBirth(LocalDate.parse(employee.getDateOfBirth()));
        employeeDto.setName(employee.getName());
        employeeDto.setSurname(employee.getSurname());
        employeeDtoRepository.save(employeeDto);
    }

    public void changePassword(Long pesel, String newPassword){
        EmployeeDto employeeDto=employeeDtoRepository.findById(pesel).get();
        PasswordEncoderConfig c=new PasswordEncoderConfig();
        PasswordEncoder passwordEncoder=c.passwordEncoder();
        employeeDto.setHashedPassword(passwordEncoder.encode(newPassword));
        employeeDtoRepository.save(employeeDto);
    }
}
