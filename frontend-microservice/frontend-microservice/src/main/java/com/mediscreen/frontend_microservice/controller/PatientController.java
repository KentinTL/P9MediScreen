package com.mediscreen.frontend_microservice.controller;

import com.mediscreen.frontend_microservice.dto.Note;
import com.mediscreen.frontend_microservice.dto.Patient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;

@Controller
public class PatientController {

    private final String GATEWAY_URL = "http://localhost:8080";
    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/")
    public String home() {
        return "redirect:/patients";
    }

    @GetMapping("/patients")
    public String showPatientList(Model model) {
        try {
            Patient[] patients = restTemplate.getForObject(GATEWAY_URL + "/api/patient/patients", Patient[].class);
            model.addAttribute("patients", patients != null ? Arrays.asList(patients) : Collections.emptyList());
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("patients", Collections.emptyList());
            model.addAttribute("error", "Impossible de récupérer la liste des patients.");
        }
        return "patient-list";
    }

    @GetMapping("/patients/{id}")
    public String showPatientDetails(@PathVariable("id") int id, Model model) {
        try {
            Patient patient = restTemplate.getForObject(GATEWAY_URL + "/api/patient/patients/" + id, Patient.class);
            model.addAttribute("patient", patient);

            Note[] notes = restTemplate.getForObject(GATEWAY_URL + "/api/notes?patientId=" + id, Note[].class);
            model.addAttribute("notes", notes != null ? Arrays.asList(notes) : Collections.emptyList());

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Impossible de récupérer les détails du patient ou ses notes.");
            return "patient-list"; // Redirige vers la liste en cas d'erreur
        }
        model.addAttribute("newNote", new Note());
        return "patient-details";
    }

    @PostMapping("/patients/{id}/notes")
    public String addNoteToPatient(@PathVariable("id") int id, @ModelAttribute Note newNote) {
        newNote.setPatientId(id);
        try {
            restTemplate.postForObject(GATEWAY_URL + "/api/notes", newNote, Note.class);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/patients/" + id + "?noteError";
        }
        return "redirect:/patients/" + id;
    }

    @GetMapping("/patients/add")
    public String showAddPatientForm(Model model) {
        model.addAttribute("patient", new Patient());
        return "add-patient";
    }

    @PostMapping("/patients/add")
    public String addPatient(@ModelAttribute Patient patient) {
        try {
            restTemplate.postForObject(GATEWAY_URL + "/api/patient/patients", patient, Patient.class);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/patients/add?error";
        }
        return "redirect:/patients";
    }

    @GetMapping("/patients/edit/{id}")
    public String showEditPatientForm(@PathVariable("id") int id, Model model) {
        try {
            Patient patient = restTemplate.getForObject(GATEWAY_URL + "/api/patient/patients/" + id, Patient.class);
            model.addAttribute("patient", patient);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/patients?error";
        }
        return "edit-patient";
    }

    @PostMapping("/patients/edit/{id}")
    public String editPatient(@PathVariable("id") int id, @ModelAttribute Patient patient) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Patient> entity = new HttpEntity<>(patient, headers);
        try {
            restTemplate.exchange(GATEWAY_URL + "/api/patient/patients/" + id, HttpMethod.PUT, entity, Void.class);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/patients/edit/" + id + "?error";
        }
        return "redirect:/patients";
    }
}