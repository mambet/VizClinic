package ru.viz.clinic.service;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.viz.clinic.data.entity.Department;
import ru.viz.clinic.data.entity.Equipment;
import ru.viz.clinic.data.repository.EquipmentRepository;
import ru.viz.clinic.help.Helper;

import java.util.List;
import java.util.Objects;

import static ru.viz.clinic.help.Translator.MSG_EQUIPMENT_SUCCESS_SAVED;

@Service
@Slf4j
public class EquipmentService {
    private final EquipmentRepository equipmentRepository;

    public EquipmentService(final EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    @Transactional
    public void save(@NotNull final Equipment equipment) {
        Objects.requireNonNull(equipment);
        try {
            equipmentRepository.save(equipment);
            Helper.showSuccessNotification(MSG_EQUIPMENT_SUCCESS_SAVED);
        } catch (final Exception e) {
            log.error("error ", e);
            Helper.showErrorNotification("ошибка при сохранении оборудования");
        }
    }

    public List<Equipment> getAll() {
        return equipmentRepository.findAll();
    }

    public Equipment getById(final Long id) {
        return equipmentRepository.findById(id).orElse(null);
    }

    public void deleteById(final Long id) {
        equipmentRepository.deleteById(id);
    }

    public List<Equipment> getByDepartment(@NotNull final Department department) {
        return equipmentRepository.getByDepartmentId(Objects.requireNonNull(department).getId());
    }
}