package edu.ib.service;

import edu.ib.object.employee.EmployeeDto;
import edu.ib.repository.EmployeeDtoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeDtoService {

    public EmployeeDtoRepository employeeDtoRepository;

    @Autowired
    public EmployeeDtoService(EmployeeDtoRepository employeeDtoRepository) {
        this.employeeDtoRepository = employeeDtoRepository;
    }

    public void addEmployee(EmployeeDto employeeDto){
        employeeDtoRepository.save(employeeDto);
    }
}
