

package com.JoyBoy.ToDo.service;



import com.JoyBoy.ToDo.Models.*;
import com.JoyBoy.ToDo.Repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;


@Service
public class CustomUserDetailsService implements UserDetailsService{

    @Autowired 
    private UserRepository userRepo;


    @Override
    public UserDetails loadUserByUsername (String userName){
    
            User user = userRepo.findByUserName(userName).orElseThrow (() -> new UsernameNotFoundException("username not found" + userName));

            UserDetails userDetails =  org.springframework.security.core.userdetails.User.builder().
                                                                                                    username(user.getUserName()).
                                                                                                    password(user.getPassword()).
                                                                                                    authorities(new SimpleGrantedAuthority(user.getRole())).
                                                                                                    build();
                                                                                                
            return userDetails;
                                                                                                   
            
                    

                                                                                            



    }
    
}