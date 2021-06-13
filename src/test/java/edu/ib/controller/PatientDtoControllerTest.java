package edu.ib.controller;

import edu.ib.object.employee.Employee;
import edu.ib.object.patient.Patient;
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
class PatientDtoControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    @WithMockUser(roles="PATIENT")
    void getPatientRegistration() throws Exception {
        long millis = System.currentTimeMillis();
        String token = Jwts.builder()
                .setSubject("92042128428")
                .claim("role", "ROLE_PATIENT")
                .setIssuedAt(new Date(millis))
                .setExpiration(new Date(millis + 1000 * 60 * 15))
                .signWith(SignatureAlgorithm.HS512, "z3gHeX23")
                .compact();
        mockMvc.perform(MockMvcRequestBuilders.get("/patient/registration").cookie(new Cookie("token", token)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser()
    void createPatient() throws Exception {

        long millis = System.currentTimeMillis();
        String token = Jwts.builder()
                .setSubject("92042128428")
                .claim("role", "ROLE_PATIENT")
                .setIssuedAt(new Date(millis))
                .setExpiration(new Date(millis + 1000 * 60 * 15))
                .signWith(SignatureAlgorithm.HS512, "z3gHeX23")
                .compact();
        Random r = new Random();
        Patient testPatient=new Patient((long) r.nextInt(10000000), "Tpat", "Tpatsurname", "1975-05-12", 321123321, "tdoc@gmail.com", "tpass");

        mockMvc.perform(MockMvcRequestBuilders.post("/patient/registration").cookie(new Cookie("token", token))
                .flashAttr("patient", testPatient))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isFound());

    }
}