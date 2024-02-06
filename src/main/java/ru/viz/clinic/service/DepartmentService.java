package ru.viz.clinic.service;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.viz.clinic.data.entity.Department;
import ru.viz.clinic.data.entity.Equipment;
import ru.viz.clinic.data.entity.Medic;
import ru.viz.clinic.repository.DepartmentRepository;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static ru.viz.clinic.help.Helper.formatAndShowErrorMessage;
import static ru.viz.clinic.help.Helper.formatAndShowSuccessMessage;

@Service
@Slf4j
public class DepartmentService extends CommonEntityService<Department, DepartmentRepository> {
    private final MedicService medicService;
    private final EquipmentService equipmentService;

    public DepartmentService(
            @NotNull final DepartmentRepository repository,
            @NotNull final MedicService medicService,
            @NotNull final EquipmentService equipmentService
    ) {
        super(Objects.requireNonNull(repository));
        this.medicService = Objects.requireNonNull(medicService);
        this.equipmentService = Objects.requireNonNull(equipmentService);
    }

    public List<Department> getActiveByHospitalId(@NotNull final String hospitalId) {
        return repository.getByHospitalIdAndActiveIs(Objects.requireNonNull(hospitalId), true);
    }

    public List<Department> getInactiveByHospitalId(@NotNull final String hospitalId) {
        return repository.getByHospitalIdAndActiveIs(Objects.requireNonNull(hospitalId), false);
    }

    @Override
    public boolean isReadyToActivate(@NotNull final Department department) {
        Objects.requireNonNull(department);
        return isHospitalActive(department);
    }

    @Override
    public boolean isReadyToDeactivate(@NotNull final Department department) {
        Objects.requireNonNull(department);
        return isActiveEquipmentFree(department)
                && isActiveMedicFree(department);
    }

    @Override
    public boolean isReadyToDelete(@NotNull final Department department) {
        Objects.requireNonNull(department);
        return isActiveEquipmentFree(department)
                && isActiveMedicFree(department)
                && isInactiveMedicFree(department)
                && isInactiveEquipmentFree(department);
    }

    @Override
    public void showUpdateSuccessMessage(final Department department) {
        formatAndShowSuccessMessage("отделение '%s' успешно обновлено", department);
    }

    @Override
    public void showUpdateErrMessage(final Department department) {
        formatAndShowErrorMessage("ошибка при обновлении отделения '%s'", department);
    }

    @Override
    public void showCreateSuccessMessage(final Department department) {
        formatAndShowSuccessMessage("отделение '%s' успешно создано", department);
    }

    @Override
    public void showCreateErrMessage(final Department department) {
        formatAndShowErrorMessage("ошибка при создании отделения '%s'", department);
    }

    @Override
    public void showDeleteSuccessMessage(final Department department) {
        formatAndShowSuccessMessage("отделение '%s' успешно удалено", department);
    }

    @Override
    public void showDeleteErrMessage(final Department department) {
        formatAndShowErrorMessage("ошибка при удалении отделения '%s'", department);
    }

    @Override
    public void showActivateSuccessMessage(final Department department) {
        formatAndShowSuccessMessage("отделение '%s' успешно активировано", department);
    }

    @Override
    public void showActivateErrMessage(final Department department) {
        formatAndShowErrorMessage("ошибка при активации отделения '%s'", department);
    }

    @Override
    public void showDeactivateSuccessMessage(final Department department) {
        formatAndShowSuccessMessage("отделение '%s' успешно деактвировано", department);
    }

    @Override
    public void showDeactivateErrMessage(final Department department) {
        formatAndShowErrorMessage("ошибка при деактивации отделения '%s'", department);
    }

    private boolean isHospitalActive(@NotNull final Department department) {
        final boolean isHospitalActive = department.getHospital().isActive();
        if (!isHospitalActive) {
            formatAndShowErrorMessage("Клиника '%s' не активеа", department.getHospital());
            return false;
        }
        return true;
    }

    private boolean isActiveEquipmentFree(@NotNull final Department department) {
        final Set<Equipment> equipmentSet =
                equipmentService.getActiveByDepartmentId(Objects.requireNonNull(department).getId());
        if (!equipmentSet.isEmpty()) {
            formatAndShowErrorMessage("еще есть активное оборудование '%s' ", equipmentSet);
            return false;
        }
        return true;
    }

    private boolean isInactiveEquipmentFree(@NotNull final Department department) {
        final Set<Equipment> equipmentSet =
                equipmentService.getInactiveByDepartmentId(Objects.requireNonNull(department).getId());
        if (!equipmentSet.isEmpty()) {
            formatAndShowErrorMessage("еще есть не активное оборудование '%s' ", equipmentSet);
            return false;
        }
        return true;
    }

    private boolean isActiveMedicFree(@NotNull final Department department) {
        final Set<Medic> medicSet =
                medicService.getActiveByDepartmentId(Objects.requireNonNull(department).getId());
        if (!medicSet.isEmpty()) {
            formatAndShowErrorMessage("еще есть активные медики '%s' ", medicSet);
            return false;
        }
        return true;
    }

    private boolean isInactiveMedicFree(@NotNull final Department department) {
        final Set<Medic> medicSet =
                medicService.getInactiveByDepartmentId(Objects.requireNonNull(department).getId());
        if (!medicSet.isEmpty()) {
            formatAndShowErrorMessage("еще есть не активные медики '%s' ", medicSet);
            return false;
        }
        return true;
    }
}