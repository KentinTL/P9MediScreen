package com.mediscreen.assessment_microservice.client;

import com.mediscreen.assessment_microservice.config.FeignClientConfiguration;
import com.mediscreen.assessment_microservice.dto.PatientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "patient-api", url = "${gateway.url}",
        configuration = FeignClientConfiguration.class)
public interface PatientClient {
    @GetMapping("/api/patient/patients/{id}")
    PatientDTO getPatientById(@PathVariable("id") Long id);
}