package edu.ib.service;


import edu.ib.object.doctor.DoctorDto;
import edu.ib.object.employee.EmployeeDto;
import edu.ib.object.patient.PatientDto;
import edu.ib.repository.DoctorDtoRepository;
import edu.ib.repository.EmployeeDtoRepository;
import edu.ib.repository.PatientDtoRepository;
import edu.ib.security.LoginPasswordException;
import edu.ib.security.PasswordEncoderConfig;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class LogInService {

    private DoctorDtoRepository doctorDtoRepository;
    private EmployeeDtoRepository employeeDtoRepository;
    private PatientDtoRepository patientDtoRepository;

    @Autowired
    public LogInService(DoctorDtoRepository doctorDtoRepository, EmployeeDtoRepository employeeDtoRepository, PatientDtoRepository patientDtoRepository) {
        this.doctorDtoRepository = doctorDtoRepository;
        this.employeeDtoRepository = employeeDtoRepository;
        this.patientDtoRepository = patientDtoRepository;
    }




    public String loginUser(Long login, String password) throws LoginPasswordException {
        String role=testDoctor(login,password);
        if(role==null) role=testPatient(login, password);
        if(role==null) role=testEmployee(login, password);

        if(role==null){
            throw new LoginPasswordException("Wrong login or Password");
        }
        long millis=System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(String.valueOf(login))
                .claim("role",role)
                .setIssuedAt(new Date(millis))
                .setExpiration(new Date(millis+1000*60*15))
                .signWith(SignatureAlgorithm.HS512,"z3gHeX23")
                .compact();

    }

    public String getUserEmailById(Long id){
        String email=null;
        if(patientDtoRepository.findById(id).isPresent()) email=patientDtoRepository.findById(id).get().getEmail();
        if(doctorDtoRepository.findById(id).isPresent()) email=doctorDtoRepository.findById(id).get().getEmail();
        if(employeeDtoRepository.findById(id).isPresent()) email=employeeDtoRepository.findById(id).get().getEmail();
        return email;
    }

    public void changePassword(Long id, String hashedPassword){
        patientDtoRepository.changePassword(id,hashedPassword);
        doctorDtoRepository.changePassword(id,hashedPassword);
        employeeDtoRepository.changePassword(id,hashedPassword);
    }

    private String testDoctor(Long login, String password){
        Optional<DoctorDto> doctor=doctorDtoRepository.findById(login);
        String output=null;

        PasswordEncoderConfig p=new PasswordEncoderConfig();
        PasswordEncoder passwordEncoder=p.passwordEncoder();

        if(doctor.isPresent()){
            if(passwordEncoder.matches(password,doctor.get().getHashedPassword())){
                output="ROLE_DOCTOR";
            }
        }

        return output;
    }

    private String testPatient(Long login, String password){
        Optional<PatientDto> patient=patientDtoRepository.findById(login);
        String output=null;

        PasswordEncoderConfig p=new PasswordEncoderConfig();
        PasswordEncoder passwordEncoder=p.passwordEncoder();

        if(patient.isPresent()){
            if(passwordEncoder.matches(password,patient.get().getHashedPassword())){
                output="ROLE_PATIENT";
            }
        }

        return output;
    }

    private String testEmployee(Long login, String password){
        Optional<EmployeeDto> employee=employeeDtoRepository.findById(login);
        String output=null;

        PasswordEncoderConfig p=new PasswordEncoderConfig();
        PasswordEncoder passwordEncoder=p.passwordEncoder();

        if(employee.isPresent()){
            if(passwordEncoder.matches(password,employee.get().getHashedPassword())){
                output="ROLE_"+employee.get().getPosition().toUpperCase();
            }
        }

        return output;
    }
}
