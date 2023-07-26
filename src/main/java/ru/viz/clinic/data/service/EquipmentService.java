package ru.viz.clinic.data.service;

import org.springframework.stereotype.Service;
import ru.viz.clinic.data.entity.Equipment;
import ru.viz.clinic.data.entity.Hospital;
import ru.viz.clinic.data.repository.EquipmentRepository;

import java.util.List;

@Service
public class EquipmentService {
    private final EquipmentRepository equipmentRepository;

    public EquipmentService(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    // Save or update a equipment
    public Equipment save(Equipment equipment) {
        return equipmentRepository.save(equipment);
    }

    // Get all hospitals
    public List<Equipment> getAll() {
        return equipmentRepository.findAll();
    }

    // Get hospital by ID
    public Equipment getById(Long id) {
        return equipmentRepository.findById(id).orElse(null);
    }

    // Delete a hospital by ID
    public void deleteById(Long id) {
        equipmentRepository.deleteById(id);
    }
}