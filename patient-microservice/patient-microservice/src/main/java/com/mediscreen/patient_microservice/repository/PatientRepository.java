package com.mediscreen.patient_microservice.repository;

import com.mediscreen.patient_microservice.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    // JpaRepository nous fournit automatiquement les méthodes
    // comme findAll(), findById(), save(), etc.
}
