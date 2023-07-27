package ru.viz.clinic.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.viz.clinic.data.entity.Department;
import ru.viz.clinic.data.repository.DepartmentRepository;

import java.util.List;

@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    // Save or update a hospital
    public Department save(Department hospital) {
        return departmentRepository.save(hospital);
    }

    // Get all hospitals
    public List<Department> getAll() {
        return departmentRepository.findAll();
    }

    // Get hospital by ID
    public Department getHospitalDepartmentById(Long id) {
        return departmentRepository.findById(id).orElse(null);
    }

    // Delete a hospital by ID
    public void deleteHospitalById(Long id) {
        departmentRepository.deleteById(id);
    }
}