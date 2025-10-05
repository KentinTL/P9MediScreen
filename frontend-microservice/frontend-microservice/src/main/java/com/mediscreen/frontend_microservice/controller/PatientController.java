package com.mediscreen.frontend_microservice.controller;

import com.mediscreen.frontend_microservice.dto.Note;
import com.mediscreen.frontend_microservice.dto.Patient;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Controller
public class PatientController {

    private final String GATEWAY_URL = "http://localhost:8080";
    private final RestTemplate restTemplate;

    public PatientController(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.basicAuthentication("user", "password").build();
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
        String notesUrl = GATEWAY_URL + "/notes?patientId=" + id;
        Patient patient = null;
        List<Note> notes = Collections.emptyList();

        try {
            patient = restTemplate.getForObject(patientUrl, Patient.class);

            Note[] notesArray = restTemplate.getForObject(notesUrl, Note[].class);
            if (notesArray != null) {
                notes = Arrays.asList(notesArray);
            }

        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des détails du patient " + id + ": " + e.getMessage());
        }

        model.addAttribute("patient", patient);
        model.addAttribute("notes", notes);

        return "patient-details";
    }

    @GetMapping("/patients/add")
    public String showAddPatientForm(Model model) {
        model.addAttribute("patient", new Patient());
        return "add-patient";
    }

    @PostMapping("/patients/add")
    public String addPatient(@ModelAttribute Patient patient) {
        String addUrl = GATEWAY_URL + "/patients";
        try {
            restTemplate.postForObject(addUrl, patient, Patient.class);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'ajout du patient : " + e.getMessage());
        }
        return "redirect:/patients";
    }

    @GetMapping("/patients/edit/{id}")
    public String showEditPatientForm(@PathVariable String id, Model model) {
        String patientUrl = GATEWAY_URL + "/patients/" + id;
        Patient patient = null;
        try {
            patient = restTemplate.getForObject(patientUrl, Patient.class);
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération du patient pour modification : " + e.getMessage());
            // Si le patient n'est pas trouvé, on redirige vers la liste
            return "redirect:/patients";
        }
        model.addAttribute("patient", patient);
        return "edit-patient";
    }

    @PostMapping("/patients/update/{id}")
    public String updatePatient(@PathVariable String id, @ModelAttribute Patient patient) {
        String updateUrl = GATEWAY_URL + "/patients/" + id;
        try {
            restTemplate.put(updateUrl, patient);
        } catch (Exception e) {
            System.err.println("Erreur lors de la mise à jour du patient : " + e.getMessage());
        }
        return "redirect:/patients/" + id;
    }
}
