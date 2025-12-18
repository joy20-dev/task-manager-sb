package com.JoyBoy.ToDo.Models;




import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import com.JoyBoy.ToDo.Models.Task;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Users")
@Data
public class User{
    @Id 
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;

    @Column(unique=true)
    private String userName;
    private String password;
    private String role ="ROLE_USER";

    // mapping tasks to logged in user
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Task> tasks = new ArrayList<>();
}