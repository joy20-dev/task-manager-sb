package com.JoyBoy.ToDo.Models;



import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;


@Entity

@Table(name="tasks")
@Data
public class Task {
	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	private Long id;
	private String title;
	private String description;
	private boolean completed = false;

	@ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
