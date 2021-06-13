package edu.ib.controller;

import edu.ib.email.EmailServiceImpl;
import edu.ib.object.LoginLog;
import edu.ib.otherModels.NewPasswordModel;
import edu.ib.otherModels.PasswordResetModel;
import edu.ib.security.DataTokenReader;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Date;

@Controller
public class LoginController {

    private LogInService logInService;
    private EmailServiceImpl emailService;
    private LoginLogsService loginLogsService;

    @Value("${jwt.login.token.key}")
    private String signingKey;

    @Value("${jwt.resetPassword.token.key}")
    private String resetPasswordSigningKey;

    @Autowired
    public LoginController(LogInService logInService,EmailServiceImpl emailService,LoginLogsService loginLogsService) {
        this.logInService = logInService;
        this.emailService=emailService;
        this.loginLogsService=loginLogsService;
    }

    @GetMapping("/login")
    public String getLoginForm(Model model, HttpServletRequest request){
        setRoleToModel(model,request);
        model.addAttribute("logger", new Logger());
        return "log_form";
    }

    @GetMapping("/moveToLogin")
    public String moveToLogin(){
        return "redirect:/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute Logger logger, Model model, HttpServletRequest request, HttpServletResponse response){
        String token;
        LoginLog log=new LoginLog();
        log.setIp(request.getRemoteAddr());
        log.setPesel(Long.parseLong(logger.getLogin()));
        log.setTimestamp(LocalDateTime.now());
        try {
            token = logInService.loginUser(Long.parseLong(logger.getLogin()), logger.getPassword());
            log.setSuccess(true);
            loginLogsService.addLog(log);
        } catch(LoginPasswordException | NumberFormatException e ) {
            model.addAttribute("logger", new Logger());
            model.addAttribute("loginError", "Błędny login lub hasło");
            log.setSuccess(false);
            loginLogsService.addLog(log);
            return "log_form";
        }
        Cookie cookie=new Cookie("token",token);
        cookie.setMaxAge(60*60);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return "redirect:/home";
    }

    @GetMapping("/passwordReset")
    public String getPasswordReset(Model model, HttpServletRequest request){
        setRoleToModel(model,request);
        model.addAttribute("passwordModel",new PasswordResetModel());
        return "reset_password";
    }

    @PostMapping("/passwordReset")
    public String postResetPassword(@ModelAttribute PasswordResetModel passwordModel){
        Long pesel=Long.parseLong(passwordModel.getPesel());
        String email=logInService.getUserEmailById(pesel);
        if(email!=null) {
            long millis = System.currentTimeMillis();
            String token = Jwts.builder()
                    .claim("pesel", pesel)
                    .setIssuedAt(new Date(millis))
                    .setExpiration(new Date(millis + 1000 * 60 * 60)) //ważny 1h
                    .signWith(SignatureAlgorithm.HS512, resetPasswordSigningKey)
                    .compact();
            emailService.sendPasswordReset(email,token);
            return "redirect:/passwordResetForm";
        } else {
            return "redirect:/error";
        }
    }

    @GetMapping("/passwordResetForm")
    public String getPasswordResetForm(Model model, HttpServletRequest request){
        setRoleToModel(model,request);
        model.addAttribute("passwordReset",new NewPasswordModel());
        return "new_reset_password";
    }

    @PostMapping("/passwordResetForm")
    public String getPasswordResetForm(@ModelAttribute NewPasswordModel passwordReset,Model model){
        Jws<Claims> claims=Jwts.parser().setSigningKey(resetPasswordSigningKey).parseClaimsJws(passwordReset.getResetToken());
        Long pesel=Long.parseLong(claims.getBody().get("pesel").toString());
        PasswordEncoderConfig c=new PasswordEncoderConfig();
        PasswordEncoder encoder=c.passwordEncoder();
        logInService.changePassword(pesel,encoder.encode(passwordReset.getNewPassword()));
        return "redirect:/home";
    }

    @GetMapping("/logouts")
    public String logout(HttpServletResponse response){
        Cookie cookie=new Cookie("token","");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return "redirect:/home";
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
