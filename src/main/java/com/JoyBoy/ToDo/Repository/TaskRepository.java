package com.JoyBoy.ToDo.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.JoyBoy.ToDo.Models.Task;
import com.JoyBoy.ToDo.Models.User;

public interface TaskRepository extends JpaRepository<Task, Long>{
    
    List<Task> findByUser(User user);

    Optional<Task> findByUserAndId(User user, Long Id);
    
    List<Task> findByUserAndCompleted(User user, boolean completed);

    void deleteByUserAndId(User user,Long Id);

    
    

}
