package com.mediscreen.notes_microservice;

import com.mediscreen.notes_microservice.model.Note;
import com.mediscreen.notes_microservice.repository.NoteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class NotesMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotesMicroserviceApplication.class, args);
	}

}
