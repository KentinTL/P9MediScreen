package com.mediscreen.patient_microservice.repository;

import com.mediscreen.patient_microservice.model.Patient;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends MongoRepository<Patient, String> {
    // Spring Data MongoDB nous fournit automatiquement les méthodes
    // comme findAll(), findById(), save(), etc.
}
