package com.mediscreen.assessment_microservice.client;

import com.mediscreen.assessment_microservice.config.FeignClientConfiguration;
import com.mediscreen.assessment_microservice.dto.NoteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "notes-api", url = "${gateway.url}",
        configuration = FeignClientConfiguration.class)
public interface NotesClient {
    @GetMapping("/api/notes")
    List<NoteDTO> getNotesByPatientId(@RequestParam("patientId") Long patientId);
}