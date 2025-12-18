package com.JoyBoy.ToDo.DTO;



public record TaskDTO(
    Long id,
    String title,
    String description,
    boolean completed
) {}