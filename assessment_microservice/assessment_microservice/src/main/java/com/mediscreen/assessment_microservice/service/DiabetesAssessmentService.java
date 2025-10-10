package com.mediscreen.assessment_microservice.service;

import com.mediscreen.assessment_microservice.client.NotesClient;
import com.mediscreen.assessment_microservice.client.PatientClient;
import com.mediscreen.assessment_microservice.dto.NoteDTO;
import com.mediscreen.assessment_microservice.dto.PatientDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.List;

@Service
public class DiabetesAssessmentService {

    @Autowired
    private PatientClient patientClient;

    @Autowired
    private NotesClient notesClient;

    private static final List<String> TRIGGERS = Arrays.asList(
            "hémoglobine a1c", "microalbumine", "taille", "poids",
            "fumeur", "fumeuse", "anormal", "cholestérol", "vertiges",
            "rechute", "réaction", "anticorps"
    );

    public String assessDiabetesRisk(Long patientId) {
        PatientDTO patient = patientClient.getPatientById(patientId);
        List<NoteDTO> notes = notesClient.getNotesByPatientId(patientId);

        int age = Period.between(patient.getBirthdate(), LocalDate.now()).getYears();

        long triggerCount = countTriggersInNotes(notes);

        if(patientId == 7) {
            System.out.println(patientId + " => " + triggerCount + " / " + age);
        }

        if (isEarlyOnset(patient, age, triggerCount)) {
            return "Early Onset";
        }
        else if (isInDanger(patient, age, triggerCount)) {
            return "In Danger";
        }
        else if (isBorderline(age, triggerCount)) {
            return "Borderline";
        }

        return "None";
    }

    private long countTriggersInNotes(List<NoteDTO> notes) {
        if (notes.isEmpty()) {
            return 0;
        }

        return notes.stream()
                .map(note -> note.getNote().toLowerCase())
                .flatMap(noteText -> TRIGGERS.stream().filter(noteText::contains))
                .count();
    }


    private boolean isBorderline(int age, long triggerCount) {
        return age > 30 && triggerCount >= 2 && triggerCount <= 5;
    }

    private boolean isInDanger(PatientDTO patient, int age, long triggerCount) {
        boolean isMale = "M".equalsIgnoreCase(patient.getGender());
        boolean isFemale = "F".equalsIgnoreCase(patient.getGender());

        if(age < 30) {
            return isMale && triggerCount == 3 || isFemale && triggerCount == 4;
        }
        else if(age > 30) {
            return triggerCount == 6 || triggerCount == 7;
        }

        return false;
    }

    private boolean isEarlyOnset(PatientDTO patient, int age, long triggerCount) {
        boolean isMale = "M".equalsIgnoreCase(patient.getGender());
        boolean isFemale = "F".equalsIgnoreCase(patient.getGender());

        if(age < 30) {
            return isMale && triggerCount >= 5 || isFemale && triggerCount >= 7;
        }
        else if(age > 30) {
            return triggerCount >= 8;
        }

        return false;
    }
}