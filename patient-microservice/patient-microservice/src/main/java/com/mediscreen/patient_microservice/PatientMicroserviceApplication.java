package com.mediscreen.patient_microservice;

import com.mediscreen.patient_microservice.model.Patient;
import com.mediscreen.patient_microservice.repository.PatientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class PatientMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PatientMicroserviceApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(PatientRepository repository) {
		return args -> {
			repository.deleteAll();

			Patient p1 = new Patient();
			p1.setFirstName("TestNone");
			p1.setLastName("Test");
			p1.setBirthdate(LocalDate.parse("1966-12-31"));
			p1.setGender("F");
			p1.setAddress("1 Brookside St");
			p1.setPhoneNumber("100-222-3333");
			repository.save(p1);

			Patient p2 = new Patient();
			p2.setFirstName("TestBorderline");
			p2.setLastName("Test");
			p2.setBirthdate(LocalDate.parse("1945-06-24"));
			p2.setGender("M");
			p2.setAddress("2 High St");
			p2.setPhoneNumber("200-333-4444");
			repository.save(p2);

			Patient p3 = new Patient();
			p3.setFirstName("TestInDanger");
			p3.setLastName("Test");
			p3.setBirthdate(LocalDate.parse("2004-06-18"));
			p3.setGender("M");
			p3.setAddress("3 Club Road");
			p3.setPhoneNumber("300-444-5555");
			repository.save(p3);

			Patient p4 = new Patient();
			p4.setFirstName("TestEarlyOnset");
			p4.setLastName("Test");
			p4.setBirthdate(LocalDate.parse("2002-06-28"));
			p4.setGender("F");
			p4.setAddress("4 Valley Dr");
			p4.setPhoneNumber("400-555-6666");
			repository.save(p4);
		};
	}
}
