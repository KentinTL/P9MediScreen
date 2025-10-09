package com.mediscreen.notes_microservice.service;

import com.mediscreen.notes_microservice.exception.ResourceNotFoundException;
import com.mediscreen.notes_microservice.model.Note;
import com.mediscreen.notes_microservice.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;

    public List<Note> getNotesByPatientId(Integer patientId) {
        return noteRepository.findByPatientId(patientId);
    }

    public Note createNote(Note note) {
        return noteRepository.save(note);
    }

    public Note updateNote(String id, Note note) {
        Note existingNote = noteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note non trouvée avec l'id : " + id));

        existingNote.setPatient(note.getPatient());
        existingNote.setNote(note.getNote());

        return noteRepository.save(existingNote);
    }

    public void deleteNote(String id) {
        noteRepository.deleteById(id);
    }
}
