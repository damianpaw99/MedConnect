package edu.ib.controller;

import edu.ib.security.SessionException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/error")
    public String sessionError(){
        return "log_form";
    }
}
