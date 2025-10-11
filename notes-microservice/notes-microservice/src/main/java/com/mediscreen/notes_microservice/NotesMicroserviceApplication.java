package com.mediscreen.notes_microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class NotesMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotesMicroserviceApplication.class, args);
	}

}
