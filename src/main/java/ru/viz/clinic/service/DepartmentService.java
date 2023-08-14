package ru.viz.clinic.service;

import jakarta.validation.constraints.NotNull;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.viz.clinic.data.entity.Department;
import ru.viz.clinic.data.repository.DepartmentRepository;
import ru.viz.clinic.help.Helper;
import ru.viz.clinic.help.Translator;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentService(final DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Transactional
    public void save(@NotNull final Department department) {
        Objects.requireNonNull(department);
        try {
            Helper.showSuccessNotification(Translator.MSG_DEPARTMENT_SUCCESS_SAVED);
            departmentRepository.save(department);
        } catch (final Exception e) {
            log.error("error ", e);
            Helper.showErrorNotification("ошибка при сохранеинии отделения");
        }
    }

    public List<Department> getAll() {
        return departmentRepository.findAll();
    }

    public List<Department> getByHospital(@NotNull final Long hospitalId) {
        return departmentRepository.getByHospitalId(hospitalId);
    }

    public Department getById(final Long id) {
        return departmentRepository.findById(id).orElse(null);
    }

    public void deleteHospitalById(final Long id) {
        departmentRepository.deleteById(id);
    }
}