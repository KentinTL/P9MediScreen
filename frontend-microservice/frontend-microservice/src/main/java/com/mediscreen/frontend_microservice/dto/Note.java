package com.mediscreen.frontend_microservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
@NoArgsConstructor
public class Note {

    private String id;
    private Integer patientId;
    private String note; // C'est le contenu de la note

    /**
     * Méthode utilitaire pour extraire la date de création à partir de l'ID MongoDB.
     * L'ID MongoDB contient le timestamp de sa création.
     * @return La date et l'heure de création de la note.
     */
    public LocalDateTime getCreationDate() {
        if (id == null || id.length() < 8) {
            return LocalDateTime.now(); // Valeur par défaut
        }
        long timestamp = Long.parseLong(this.id.substring(0, 8), 16);
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault());
    }
}