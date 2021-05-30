package edu.ib.controller;

import edu.ib.object.doctor.Doctor;
import edu.ib.object.employee.Employee;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.servlet.http.Cookie;
import java.util.Date;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeDtoControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    @WithMockUser(roles="EMPLOYEE")
    void getEmployeeRegistration() throws Exception {
        long millis = System.currentTimeMillis();
        String token = Jwts.builder()
                .setSubject("68090151712")
                .claim("role", "ROLE_EMPLOYEE")
                .setIssuedAt(new Date(millis))
                .setExpiration(new Date(millis + 1000 * 60 * 15))
                .signWith(SignatureAlgorithm.HS512, "z3gHeX23")
                .compact();
        mockMvc.perform(MockMvcRequestBuilders.get("/employee/admin/registration").cookie(new Cookie("token", token)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser()
    void createEmployee() throws Exception {

        long millis = System.currentTimeMillis();
        String token = Jwts.builder()
                .setSubject("88080876821")
                .claim("role", "ROLE_DOCTOR")
                .setIssuedAt(new Date(millis))
                .setExpiration(new Date(millis + 1000 * 60 * 15))
                .signWith(SignatureAlgorithm.HS512, "z3gHeX23")
                .compact();
        Random r = new Random();
        Employee testEmployee=new Employee((long) r.nextInt(10000000), "TDoc", "Tdocsurname", "1975-05-12", 321123321, "tdoc@gmail.com","recepcja", "tpass");

        mockMvc.perform(MockMvcRequestBuilders.post("/employee/admin/registration").cookie(new Cookie("token", token))
                .flashAttr("employee", testEmployee))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isFound());

    }
}