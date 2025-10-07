package com.mediscreen.notes_microservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "notes")
public class Note {

    @Id
    private String id;

    @Field("patientId")
    private Integer patientId;

    @Field("patient")
    private String patient;

    @Field("note")
    private String note;
}