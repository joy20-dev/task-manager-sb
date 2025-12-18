package com.JoyBoy.ToDo.Controller;

import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.stereotype.Controller;

import com.JoyBoy.ToDo.Models.Task;

@Controller
public class LoginController{


    @GetMapping("/")
    public String getHomePage(){
        return "login";
    }

    @GetMapping("/login")
    public String getLoginPage(){
        return "login"; // should render the html login page
    }
}