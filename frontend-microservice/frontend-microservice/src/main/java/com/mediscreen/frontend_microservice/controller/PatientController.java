package com.mediscreen.frontend_microservice.controller;
import com.mediscreen.frontend_microservice.dto.Patient;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Controller
public class PatientController {

    private final String GATEWAY_URL = "http://localhost:8080";
    private final RestTemplate restTemplate;

    // On configure le RestTemplate pour qu'il s'authentifie
    public PatientController(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder
                .basicAuthentication("user", "password")
                .build();
    }

    @GetMapping("/patients")
    public String showPatientList(Model model) {
        String patientsUrl = GATEWAY_URL + "/patients";
        List<Patient> patients = Collections.emptyList();

        try {
            Patient[] patientsArray = restTemplate.getForObject(patientsUrl, Patient[].class);
            if (patientsArray != null) {
                patients = Arrays.asList(patientsArray);
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des patients : " + e.getMessage());
        }

        model.addAttribute("patients", patients);
        return "patient-list";
    }

    @GetMapping("/patients/{id}")
    public String showPatientDetails(@PathVariable String id, Model model) {
        String patientUrl = GATEWAY_URL + "/patients/" + id;
        Patient patient = null;

        try {
            // On appelle la Gateway pour récupérer les infos d'un seul patient
            patient = restTemplate.getForObject(patientUrl, Patient.class);
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération du patient " + id + ": " + e.getMessage());
        }

        // On passe le patient (ou null s'il n'est pas trouvé) à la page HTML
        model.addAttribute("patient", patient);

        // On retourne le nom du nouveau fichier HTML à afficher : "patient-details.html"
        return "patient-details";
    }
}
