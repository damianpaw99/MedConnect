package edu.ib.controller;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import edu.ib.email.EmailServiceImpl;
import edu.ib.object.LoginLogs;
import edu.ib.otherModels.NewPasswordModel;
import edu.ib.otherModels.PasswordResetModel;
import edu.ib.security.Logger;
import edu.ib.security.LoginPasswordException;
import edu.ib.security.PasswordEncoderConfig;
import edu.ib.service.LogInService;
import edu.ib.service.LoginLogsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;

@Controller
public class LoginController {

    private LogInService logInService;
    private EmailServiceImpl emailService;
    private LoginLogsService loginLogsService;

    @Autowired
    public LoginController(LogInService logInService,EmailServiceImpl emailService,LoginLogsService loginLogsService) {
        this.logInService = logInService;
        this.emailService=emailService;
        this.loginLogsService=loginLogsService;
    }

    @GetMapping("/login")
    public String getLoginForm(Model model){
        model.addAttribute("logger", new Logger());
        return "log_form";
    }


    @PostMapping("/login")
    public String login(@ModelAttribute Logger logger, Model model, HttpServletRequest request, HttpServletResponse response){
        String token=null;
        LoginLogs logs=new LoginLogs();
        logs.setIp(request.getRemoteAddr());
        logs.setPesel(Long.parseLong(logger.getLogin()));
        logs.setTimestamp(LocalDateTime.now());
        try {
            token = logInService.loginUser(Long.parseLong(logger.getLogin()), logger.getPassword());
            logs.setSuccess(true);
            loginLogsService.addLog(logs);
        } catch(LoginPasswordException | NumberFormatException e ) {
            model.addAttribute("logger", new Logger());
            model.addAttribute("loginError", "Błędny login lub hasło");
            logs.setSuccess(false);
            loginLogsService.addLog(logs);
            return "log_form";
        }
        Cookie cookie=new Cookie("token",token);
        cookie.setMaxAge(60*15);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return "redirect:/home";
    }

    @GetMapping("/passwordReset")
    public String getPasswordReset(Model model){
        model.addAttribute("passwordModel",new PasswordResetModel());
        return "reset_password";
    }

    @PostMapping("/passwordReset")
    public String postResetPassword(@ModelAttribute PasswordResetModel passwordModel, Model model){
        Long pesel=Long.parseLong(passwordModel.getPesel());
        String email=logInService.getUserEmailById(pesel);
        if(email!=null) {
            long millis = System.currentTimeMillis();
            String token = Jwts.builder()
                    .claim("pesel", pesel)
                    .setIssuedAt(new Date(millis))
                    .setExpiration(new Date(millis + 1000 * 60 * 60)) //ważny 1h
                    .signWith(SignatureAlgorithm.HS512, "pjGf37gsN!")
                    .compact();
            emailService.sendPasswordReset(email,token);
            return "redirect:/passwordResetForm";
        } else {
            return "redirect:/error";
        }
    }

    @GetMapping("/passwordResetForm")
    public String getPasswordResetForm(Model model){
        model.addAttribute("passwordReset",new NewPasswordModel());
        return "new_reset_password";
    }

    @PostMapping("/passwordResetForm")
    public String getPasswordResetForm(@ModelAttribute NewPasswordModel passwordReset,Model model){
        Jws<Claims> claims=Jwts.parser().setSigningKey("pjGf37gsN!").parseClaimsJws(passwordReset.getResetToken());
        Long pesel=Long.parseLong(claims.getBody().get("pesel").toString());
        PasswordEncoderConfig c=new PasswordEncoderConfig();
        PasswordEncoder encoder=c.passwordEncoder();
        logInService.changePassword(pesel,encoder.encode(passwordReset.getNewPassword()));
        return "redirect:/home";
    }
}
