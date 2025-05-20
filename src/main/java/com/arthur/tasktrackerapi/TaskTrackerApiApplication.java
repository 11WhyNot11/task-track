package com.arthur.tasktrackerapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TaskTrackerApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskTrackerApiApplication.class, args);
	}

}
