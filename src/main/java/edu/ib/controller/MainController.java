package edu.ib.controller;


import edu.ib.security.DataTokenReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

@Controller
public class MainController {

    @Value("${jwt.login.token.key}")
    private String signingKey;

    @GetMapping("")
    public String goToMainPage(){
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String getMainPage(Model model,HttpServletRequest request){
        setRoleToModel(model, request);
        model.addAttribute("date", LocalDate.now().toString());
        return "home";
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
