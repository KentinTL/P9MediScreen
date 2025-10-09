package com.mediscreen.frontend_microservice.controller;

import com.mediscreen.frontend_microservice.dto.Note;
import com.mediscreen.frontend_microservice.dto.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
@RequestMapping("/patients")
public class PatientController {

    @Value("${gateway.url}")
    private String gatewayUrl;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/")
    public String home() {
        return "redirect:/patients";
    }

    @GetMapping
    public String showPatientList(Model model) {
        try {
            Patient[] patients = restTemplate.getForObject(gatewayUrl + "/api/patient/patients", Patient[].class);
            model.addAttribute("patients", patients != null ? Arrays.asList(patients) : Collections.emptyList());
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("patients", Collections.emptyList());
            model.addAttribute("error", "Impossible de récupérer la liste des patients.");
        }
        return "patient-list";
    }

    @GetMapping("/{id}")
    public String showPatientDetails(@PathVariable("id") int id, Model model) {
        try {
            Patient patient = restTemplate.getForObject(gatewayUrl + "/api/patient/patients/" + id, Patient.class);
            model.addAttribute("patient", patient);

            Note[] notes = restTemplate.getForObject(gatewayUrl + "/api/notes?patientId=" + id, Note[].class);
            model.addAttribute("notes", notes != null ? Arrays.asList(notes) : Collections.emptyList());

            String assessmentUrl = gatewayUrl + "/assess/patient/" + id;
            String riskLevel = restTemplate.getForObject(assessmentUrl, String.class);
            model.addAttribute("riskLevel", riskLevel);

            model.addAttribute("newNote", new Note());

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Impossible de récupérer les détails du patient ou ses notes.");
            return "patient-list"; // Redirige vers la liste en cas d'erreur
        }
        model.addAttribute("newNote", new Note());
        return "patient-details";
    }

    @PostMapping("/{patientId}/notes")
    public String addNoteToPatient(@PathVariable("patientId") int patientId, @ModelAttribute Note newNote) {
        try {
            String patientUrl = gatewayUrl + "/api/patient/patients/" + patientId;
            Patient patient = restTemplate.getForObject(patientUrl, Patient.class);

            newNote.setPatientId(patientId);
            if (patient != null) {
                newNote.setPatient(patient.getFirstName());
            }

            restTemplate.postForObject(gatewayUrl + "/api/notes", newNote, Note.class);

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/patients/" + patientId + "?noteError";
        }
        return "redirect:/patients/" + patientId;
    }

    @GetMapping("/add")
    public String showAddPatientForm(Model model) {
        model.addAttribute("patient", new Patient());
        return "add-patient";
    }

    @PostMapping("/add")
    public String addPatient(@ModelAttribute Patient patient) {
        try {
            restTemplate.postForObject(gatewayUrl + "/api/patient/patients", patient, Patient.class);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/patients/add?error";
        }
        return "redirect:/patients";
    }

    @GetMapping("/edit/{id}")
    public String showEditPatientForm(@PathVariable("id") int id, Model model) {
        try {
            Patient patient = restTemplate.getForObject(gatewayUrl + "/api/patient/patients/" + id, Patient.class);
            model.addAttribute("patient", patient);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/patients?error";
        }
        return "edit-patient";
    }

    @PostMapping("/edit/{id}")
    public String editPatient(@PathVariable("id") int id, @ModelAttribute Patient patient) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Patient> entity = new HttpEntity<>(patient, headers);
        try {
            restTemplate.exchange(gatewayUrl + "/api/patient/patients/" + id, HttpMethod.PUT, entity, Void.class);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/patients/edit/" + id + "?error";
        }
        return "redirect:/patients";
    }

    @PostMapping("/delete/{id}")
    public String deletePatient(@PathVariable("id") int id) {
        try {
            String url = gatewayUrl + "/api/patient/patients/" + id;
            restTemplate.delete(url);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/patients?deleteError";
        }
        return "redirect:/patients";
    }

    @PostMapping("/{patientId}/notes/delete/{noteId}")
    public String deleteNote(@PathVariable("patientId") int patientId, @PathVariable("noteId") String noteId) {
        try {
            String url = gatewayUrl + "/api/notes/" + noteId;
            restTemplate.delete(url);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/patients/" + patientId + "?noteDeleteError";
        }
        // On redirige vers la page du patient pour voir le résultat
        return "redirect:/patients/" + patientId;
    }
}