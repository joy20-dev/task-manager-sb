package com.JoyBoy.ToDo.DTO;

import java.util.List;

public record UserDTO(
    Long id,
    String firstName,
    String lastName,
    String userName,
    String role,
    List<TaskDTO> tasks
) {}