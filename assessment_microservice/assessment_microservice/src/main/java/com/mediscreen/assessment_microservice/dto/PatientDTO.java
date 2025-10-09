package com.mediscreen.assessment_microservice.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class PatientDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthdate;
    private String gender;
}