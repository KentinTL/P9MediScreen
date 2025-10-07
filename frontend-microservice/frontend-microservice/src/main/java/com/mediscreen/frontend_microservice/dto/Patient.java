package com.mediscreen.frontend_microservice.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class Patient {
    private String id;
    private String firstName;
    private String lastName;
    private LocalDate birthdate;
    private String gender;
    private String address;
    private String phoneNumber;
}