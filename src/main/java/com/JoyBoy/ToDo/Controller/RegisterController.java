package com.JoyBoy.ToDo.Controller;

import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.Cookie;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.JoyBoy.ToDo.Models.User;
import com.JoyBoy.ToDo.service.*;
import com.JoyBoy.ToDo.DTO.*;


import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/auth")

@RequiredArgsConstructor
public class RegisterController {

    private final RegisterService registerService;
    private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public User createUser(@RequestBody User user){
        return registerService.createUser(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginReq){

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginReq.getUserName(), loginReq.getPassword()));

        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(loginReq.getUserName(),authentication.getAuthorities());
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }

    }
    
}
