package com.JoyBoy.ToDo.Controller;

import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.JoyBoy.ToDo.Models.*;
import com.JoyBoy.ToDo.service.TaskService;
import com.JoyBoy.ToDo.Repository.*;
import com.JoyBoy.ToDo.DTO.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;



// @RestController 
// @RequestMapping("/api/tasks")
// @RequiredArgsConstructor
// public class TaskController {



//     private final TaskService taskService;

     
//     @GetMapping
//     public List<Task> getTasks(){
//         return taskService.getTasks();
//     }

//     @GetMapping("/{id}")
//     public Task getTaskById(@PathVariable Long id){
//         return taskService.getTaskById(id);
        
//     }

//      @GetMapping("/completed")
//      public List<Task> completedTask(){
//         return taskService.completedTask();

//     }



    
//     @GetMapping("/pending")
//      public List<Task> pendingTask(){
//         return taskService.pendingTask();

//     }

//     @PostMapping()
//     public Task createTask(@RequestBody Task task){
//         return taskService.createTask(task);

//     }

//     @PutMapping("/{id}")
//     public Task updateTask(@PathVariable long id,@RequestBody Task newTask){
//         return taskService.updateTask(id, newTask);
//     }
    

//     @DeleteMapping("/{id}")
//     public void deleteTask(@PathVariable Long id){
//         taskService.deleteTask(id);

//     } 




// }

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final UserRepository userRepository;

    // Helper method to get logged-in user
    private User getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        return userRepository.findByUserName(username)
        .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @GetMapping
    public List<TaskDTO> getTasks() {
        User user = getLoggedInUser();
        return taskService.getTasks(user);
    }

    @GetMapping("/{id}")
    public TaskDTO getTaskById(@PathVariable Long id) {
        User user = getLoggedInUser();
        return taskService.getTaskById(user, id);
    }

    @GetMapping("/completed")
    public List<TaskDTO> completedTask(){
        User user = getLoggedInUser();
        return taskService.completedTask(user);

    }
    @GetMapping("/pending")
    public List<TaskDTO> pendingTask(){
        User user = getLoggedInUser();
        return taskService.pendingTask(user);

    }

    @PostMapping
    public TaskDTO createTask(@RequestBody TaskDTO taskDto) {
        User user = getLoggedInUser();
        return taskService.createTask(user, taskDto);
    }

    @PutMapping("/{id}")
    public TaskDTO updateTask(@PathVariable Long id, @RequestBody TaskDTO taskDto) {
        User user = getLoggedInUser();
        return taskService.updateTask(id, taskDto, user);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        User user = getLoggedInUser();
        taskService.deleteTask(user, id);
    }
}

