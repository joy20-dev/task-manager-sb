package com.JoyBoy.ToDo.Repository;


import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;

import com.JoyBoy.ToDo.Models.User;

public interface  UserRepository extends JpaRepository<User,Long>{
    Optional<User> findByUserName(String userName);
}