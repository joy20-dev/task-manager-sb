package com.JoyBoy.ToDo.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegisterPageController {

    @GetMapping("/register")
    public String tasksPage() {
        return "redirect:/register2.html";
    }
    
}
