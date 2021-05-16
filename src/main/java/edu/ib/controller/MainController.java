package edu.ib.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;

@Controller
public class MainController {

    @GetMapping("")
    public String goToMainPage(){
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String getMainPage(Model model){
        model.addAttribute("date", LocalDate.now().toString());
        return "home";
    }


}
