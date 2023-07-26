package ru.viz.clinic.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.viz.clinic.data.entity.Hospital;
import ru.viz.clinic.data.repository.HospitalRepository;

import java.util.List;

@Service
public class HospitalService {
    private final HospitalRepository hospitalRepository;

    @Autowired
    public HospitalService(HospitalRepository hospitalRepository) {
        this.hospitalRepository = hospitalRepository;
    }

    // Save or update a hospital
    public Hospital saveHospital(Hospital hospital) {
        return hospitalRepository.save(hospital);
    }

    // Get all hospitals
    public List<Hospital> getAll() {
        return hospitalRepository.findAll();
    }

    // Get hospital by ID
    public Hospital getHospitalById(Long id) {
        return hospitalRepository.findById(id).orElse(null);
    }

    // Delete a hospital by ID
    public void deleteHospitalById(Long id) {
        hospitalRepository.deleteById(id);
    }
}