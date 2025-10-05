package com.mediscreen.patient_microservice.service;

import com.mediscreen.patient_microservice.model.Patient;
import com.mediscreen.patient_microservice.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Optional<Patient> getPatientById(Long id) {
        return patientRepository.findById(id);
    }

    public Patient addPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    public Patient updatePatient(Long id, Patient patientDetails) {
        Optional<Patient> optionalPatient = patientRepository.findById(id);

        if (optionalPatient.isPresent()) {
            Patient existingPatient = optionalPatient.get();
            existingPatient.setFirstName(patientDetails.getFirstName());
            existingPatient.setLastName(patientDetails.getLastName());
            existingPatient.setBirthdate(patientDetails.getBirthdate());
            existingPatient.setGender(patientDetails.getGender());
            existingPatient.setAddress(patientDetails.getAddress());
            existingPatient.setPhoneNumber(patientDetails.getPhoneNumber());
            return patientRepository.save(existingPatient);
        } else {
            return null;
        }
    }

    public void deletePatient(Long id) {
        if(patientRepository.existsById(id)) {
            patientRepository.deleteById(id);
        }
    }
}
