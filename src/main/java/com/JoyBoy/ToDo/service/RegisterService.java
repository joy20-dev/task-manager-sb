package com.JoyBoy.ToDo.service;

import java.util.Optional;


import org.springframework.stereotype.Service;

import com.JoyBoy.ToDo.Models.User;
import com.JoyBoy.ToDo.Repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;



@Service
public class RegisterService {

    
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    


    public RegisterService(UserRepository userRepo,PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder=  passwordEncoder;
    }




    public User createUser(User user){
        Optional<User> userOp = userRepo.findByUserName(user.getUserName());
        if (user.getUserName() == "" || user.getFirstName() == "" || user.getLastName()== "" || user.getPassword() =="") {
            throw new IllegalArgumentException ("fields cannot be empty");
        }
        else{
            if(userOp.isPresent()){
                throw new IllegalArgumentException ("user name is already taken") ;
            }
            else{
                user.setPassword(passwordEncoder.encode(user.getPassword()));

                 return userRepo.save(user);
            }
        }
            

        
    }

    
}
