package com.mediscreen.notes_microservice.client;

import com.mediscreen.notes_microservice.config.FeignClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "patient-service", url = "http://patient-service:8081",
        configuration = FeignClientConfiguration.class)

public interface PatientClient {

    @GetMapping("/api/patient/patients/{id}")
    void checkPatientExists(@PathVariable("id") int id);
}