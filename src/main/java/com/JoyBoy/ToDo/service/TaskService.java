package com.JoyBoy.ToDo.service;

import com.JoyBoy.ToDo.Repository.TaskRepository;
import com.JoyBoy.ToDo.Models.Task;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List; 
import com.JoyBoy.ToDo.DTO.TaskDTO;
import com.JoyBoy.ToDo.Models.User;
import org.springframework.transaction.annotation.Transactional;



// @Service
// @RequiredArgsConstructor
// public class TaskService{
//     private final TaskRepository taskRepository;
    
//     public List<Task> getTasks(){
//         return taskRepository.findAll();
//     }

//     public Task getTaskById( Long id){
//         return taskRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "task not found"));
//     }

//     public List<Task> completedTask(){
//         return taskRepository.findByCompleted(true);

//         // return taskRepository.findByUserAndCompleted(user ,true)
//     }

//     public List<Task> pendingTask(){
//         return taskRepository.findByCompleted(false);
//     }

//     public Task createTask(Task task){
//         return taskRepository.save(task);

//     }

//     public Task updateTask( long id, Task newTask){
//         Task taskOld = getTaskById(id);

//         taskOld.setTitle(newTask.getTitle());
//         taskOld.setDescription(newTask.getDescription());
//         taskOld.setCompleted(newTask.isCompleted());

        
//         return taskRepository.save(taskOld);
//     }

//     public void deleteTask( Long id){
//         taskRepository.deleteById(id);

//     }


// }

@Service
@RequiredArgsConstructor
@Transactional

public class TaskService {

    private final TaskRepository taskRepo;


    public TaskDTO toDto(Task task){
        return new TaskDTO(
            task.getId(),
            task.getTitle(),
            task.getDescription(),
            task.isCompleted()


        );
    }

    public TaskDTO createTask(User user, TaskDTO taskDto){
        Task task = new Task();
        task.setTitle(taskDto.title());
        task.setDescription(taskDto.description());
        task.setCompleted(taskDto.completed());
        task.setUser(user);

        return toDto(taskRepo.save(task));

    }


    public List<TaskDTO> getTasks(User user){
        return taskRepo.findByUser(user).stream().map(this::toDto).toList();
    }

    public TaskDTO getTaskById(User user, Long id){
        Task task = taskRepo.findByUserAndId(user,id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "task not found"));
        return toDto(task);
    }

    

    public List<TaskDTO> completedTask(User user){
        return taskRepo.findByUserAndCompleted(user,true).stream().map(this::toDto).toList();
    }

    public List<TaskDTO> pendingTask(User user){
        return taskRepo.findByUserAndCompleted(user,false).stream().map(this::toDto).toList();
    }

    public void deleteTask(User user,Long id){
        taskRepo.deleteByUserAndId(user,id);
    }

    public TaskDTO updateTask(Long id, TaskDTO taskDto, User user){
        Task task = taskRepo.findByUserAndId(user,id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"task not found"));
        task.setTitle(taskDto.title());
        task.setDescription(taskDto.description());
        task.setCompleted(taskDto.completed());
        return toDto(taskRepo.save(task));
   
    }
    
}