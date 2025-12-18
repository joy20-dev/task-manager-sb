package com.JoyBoy.ToDo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.JoyBoy.ToDo.Repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder; 
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import com.JoyBoy.ToDo.Models.User;



@SpringBootApplication
public class ToDoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ToDoApplication.class, args);


	}

	
	@Bean
	public CommandLineRunner initUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			// check if users exist
			if (userRepository.findByUserName("admin").isEmpty()) {
				User admin = new User();
				admin.setUserName("admin");
				admin.setPassword(passwordEncoder.encode("admin123"));
				admin.setRole("ROLE_ADMIN");
				userRepository.save(admin);
			}
			
			if (userRepository.findByUserName("user").isEmpty()) {
				User user = new User();
				user.setUserName("user");
				user.setPassword(passwordEncoder.encode("user123"));
				user.setRole("ROLE_USER");
				userRepository.save(user);
			}
			System.out.println("✅ Sample users created!");
            System.out.println("   Admin: admin / admin123");
            System.out.println("   User:  user / user123");
		};
	}


}
