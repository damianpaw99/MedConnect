package edu.ib.controller;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import edu.ib.security.Logger;
import edu.ib.security.LoginPasswordException;
import edu.ib.service.LogInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.Duration;

@Controller
public class LoginController {

    private LogInService logInService;

    @Autowired
    public LoginController(LogInService logInService) {
        this.logInService = logInService;
    }

    @GetMapping("/login")
    public String getLoginForm(Model model){
        model.addAttribute("logger", new Logger());
        return "log_form";
    }


    @PostMapping("/login")
    public String login(@ModelAttribute Logger logger, Model model, HttpServletResponse response){
        String token=null;
        try {
            token = logInService.loginUser(Long.parseLong(logger.getLogin()), logger.getPassword());
        } catch(LoginPasswordException | NumberFormatException e ){
            model.addAttribute("logger", new Logger());
            model.addAttribute("loginError","Błędny login lub hasło");
            return "log_form";
        }
        Cookie cookie=new Cookie("token",token);
        cookie.setMaxAge(60*15);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return "redirect:/home";
    }
}
