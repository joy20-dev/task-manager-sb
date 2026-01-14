package com.JoyBoy.ToDo.Controller;

import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.Cookie;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import com.JoyBoy.ToDo.Models.User;
import com.JoyBoy.ToDo.service.*;
import com.JoyBoy.ToDo.DTO.*;
import java.util.Map;
import jakarta.servlet.http.HttpServletResponse;


import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/auth")

@RequiredArgsConstructor
public class RegisterController {

    private final RegisterService registerService;
    private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;

    @GetMapping("/")
    public String index() {
        return "forward:/index.html";
    }

    @PostMapping("/register")
    public User createUser(@RequestBody User user){
        return registerService.createUser(user);
    }

    // @PostMapping("/login")
    // public ResponseEntity<?> login(@RequestBody LoginRequest loginReq){
        

    //     System.out.println("in login controller");

    //     Authentication authentication = authenticationManager.authenticate(
    //         new UsernamePasswordAuthenticationToken(loginReq.getUserName(), loginReq.getPassword()));

    //     if (authentication.isAuthenticated()) {
    //         System.out.println("checking auth");
    //         String token = jwtService.generateToken(loginReq.getUserName(),authentication.getAuthorities());
    //         return ResponseEntity.ok(Map.of("token",token));

    //     } else {
    //         System.out.println("iauth failed here");
    //         // throw new UsernameNotFoundException("Invalid user request!");
    //         return ResponseEntity.ok("fucked");
            
            
    //     }
        

    // }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginReq, HttpServletResponse response) {

        System.out.println("in login controller");

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginReq.getUserName(), loginReq.getPassword())
        );

        if (authentication.isAuthenticated()) {
            System.out.println("checking auth");

            // Generate JWT
            String token = jwtService.generateToken(loginReq.getUserName(), authentication.getAuthorities());

            // Create cookie
            Cookie cookie = new Cookie("jwt", token);
            cookie.setHttpOnly(true);        // prevent JS access
            cookie.setSecure(true);          // only HTTPS in production
            cookie.setPath("/");             // available for entire app
            cookie.setMaxAge(24 * 60 * 60);  // 1 day expiration

            // Add cookie to response
            response.addCookie(cookie);

            // Optionally, also return a success message
            return ResponseEntity.ok(Map.of("message", "Login successful"));

        } else {
            System.out.println("auth failed here");
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }
    }

    
}
