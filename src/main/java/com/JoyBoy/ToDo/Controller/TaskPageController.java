package com.JoyBoy.ToDo.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class TaskPageController {

    @GetMapping("/tasks")
    public String tasksPage() {
        return "redirect:/task.html";
    }
}
