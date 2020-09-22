package com.reditt.project.reditt_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class RedittProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedittProjectApplication.class, args);
	}

}
