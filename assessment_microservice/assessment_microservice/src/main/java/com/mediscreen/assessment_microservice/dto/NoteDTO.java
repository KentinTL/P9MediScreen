
package com.mediscreen.assessment_microservice.dto;

import lombok.Data;

@Data
public class NoteDTO {
    private String id;
    private Integer patientId;
    private String note;
}