package edu.ib.service;

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
}
