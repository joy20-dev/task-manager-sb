package com.JoyBoy.ToDo.Controller;

import org.springframework.web.bind.annotation.*;

import com.JoyBoy.ToDo.Models.User;
import com.JoyBoy.ToDo.service.RegisterService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/register")

@RequiredArgsConstructor
public class RegisterController {

    private final RegisterService registerService;

    @PostMapping
    public User createUser(@RequestBody User user){
        return registerService.createUser(user);
    }
    
}
