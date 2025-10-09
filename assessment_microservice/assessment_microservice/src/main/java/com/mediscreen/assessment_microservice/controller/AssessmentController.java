package com.mediscreen.assessment_microservice.controller;

import com.mediscreen.assessment_microservice.service.DiabetesAssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/assess")
public class AssessmentController {

    @Autowired
    private DiabetesAssessmentService assessmentService;

    @GetMapping("/patient/{id}")
    public ResponseEntity<String> getAssessmentForPatient(@PathVariable("id") Long id) {
        try {
            String riskLevel = assessmentService.assessDiabetesRisk(id);
            return ResponseEntity.ok(riskLevel);
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Impossible d'évaluer le risque pour le patient ID " + id + ". Cause : " + e.getMessage());
        }
    }
}