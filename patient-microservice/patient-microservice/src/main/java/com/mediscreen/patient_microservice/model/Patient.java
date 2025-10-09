package com.mediscreen.patient_microservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

@Data
@Entity
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Le prénom ne peut pas être vide.")
    private String firstName;
    @NotBlank(message = "Le nom ne peut pas être vide.")
    private String lastName;
    @NotNull(message = "La date de naissance ne peut pas être vide.")
    private LocalDate birthdate;
    @NotBlank(message = "Le genre ne peut pas être vide.")
    private String gender;

    private String address;
    private String phoneNumber;


}