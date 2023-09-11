package ru.viz.clinic.service;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.viz.clinic.converter.EntityToStringConverter;
import ru.viz.clinic.data.entity.Department;
import ru.viz.clinic.data.entity.Engineer;
import ru.viz.clinic.data.entity.Hospital;
import ru.viz.clinic.help.Helper;
import ru.viz.clinic.repository.HospitalRepository;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static ru.viz.clinic.help.Helper.formatAndShowErrorMessage;
import static ru.viz.clinic.help.Helper.formatAndShowSuccessMessage;

@Service
@Slf4j
public class HospitalService extends CommonEntityService<Hospital, HospitalRepository> {
    private final DepartmentService departmentService;
    private final EngineerService engineerService;

    @Autowired
    public HospitalService(
            @NotNull final HospitalRepository hospitalRepository,
            @NotNull final DepartmentService departmentService,
            @NotNull final EngineerService engineerService
    ) {
        super(Objects.requireNonNull(hospitalRepository));
        this.departmentService = Objects.requireNonNull(departmentService);
        this.engineerService = Objects.requireNonNull(engineerService);
    }

    @Override
    public boolean isReadyToActivate(final Hospital hospital) {
        return true;
    }

    @Override
    public boolean isReadyToDeactivate(final Hospital hospital) {
        return isNotActiveDepartment(hospital.getId())
                && isNotActiveEngineer(hospital.getId());
    }

    @Override
    public boolean isReadyToDelete(final Hospital hospital) {
        return isNotActiveDepartment(hospital.getId())
                && isNotActiveEngineer(hospital.getId())
                && !isInactiveDepartment(hospital.getId())
                && !isInactiveEngineer(hospital.getId());
    }

    @Override
    public void showUpdateSuccessMessage(final Hospital hospital) {
        formatAndShowSuccessMessage("клиника '%s' успешно обновлена", hospital);
    }

    @Override
    public void showUpdateErrMessage(final Hospital hospital) {
        formatAndShowErrorMessage("ошибка при обновлении клиники '%s'", hospital);
    }

    @Override
    public void showCreateSuccessMessage(final Hospital hospital) {
        formatAndShowSuccessMessage("клиника '%s' успешно создана", hospital);
    }

    @Override
    public void showCreateErrMessage(final Hospital hospital) {
        formatAndShowErrorMessage("ошибка при создании клиники '%s'", hospital);
    }

    @Override
    public void showDeleteSuccessMessage(final Hospital hospital) {
        formatAndShowSuccessMessage("клиника '%s' успешно удалена", hospital);
    }

    @Override
    public void showDeleteErrMessage(final Hospital hospital) {
        formatAndShowErrorMessage("ошибка при удалении клиники '%s'", hospital);
    }

    @Override
    public void showActivateSuccessMessage(final Hospital hospital) {
        formatAndShowSuccessMessage("клиника '%s' успешно активирована", hospital);
    }

    @Override
    public void showActivateErrMessage(final Hospital hospital) {
        formatAndShowErrorMessage("ошибка при активации клиники '%s'", hospital);
    }

    @Override
    public void showDeactivateSuccessMessage(final Hospital hospital) {
        formatAndShowSuccessMessage("клиника '%s' успешно деактвирована", hospital);
    }

    @Override
    public void showDeactivateErrMessage(final Hospital hospital) {
        formatAndShowErrorMessage("ошибка при деактивации клиники '%s'", hospital);
    }

    private boolean isNotActiveEngineer(final Long hospitalId) {
        final Set<Engineer> engineers = engineerService.getActiveByHospitalId(hospitalId);
        if (!engineers.isEmpty()) {
            Helper.showErrorNotification(String.format("Еще активные инженеры: %s",
                    EntityToStringConverter.convertToPresentation(engineers)));
            return false;
        }
        return true;
    }

    private boolean isInactiveEngineer(final Long hospitalId) {
        final Set<Engineer> engineers = engineerService.getInactiveByHospitalId(hospitalId);
        if (!engineers.isEmpty()) {
            Helper.showErrorNotification(String.format("Еще не активные инженеры: %s",
                    EntityToStringConverter.convertToPresentation(engineers)));
            return true;
        }
        return false;
    }

    private boolean isNotActiveDepartment(final Long hospitalId) {
        final List<Department> departments = departmentService.getActiveByHospitalId(hospitalId);
        if (!departments.isEmpty()) {
            Helper.showErrorNotification(String.format("Еще активные отделения: %s",
                    EntityToStringConverter.convertToPresentation(departments)));
            return false;
        }
        return true;
    }

    private boolean isInactiveDepartment(final Long hospitalId) {
        final List<Department> departments = departmentService.getInactiveByHospitalId(hospitalId);
        if (!departments.isEmpty()) {
            Helper.showErrorNotification(String.format("Еще не активные отделения: %s",
                    EntityToStringConverter.convertToPresentation(departments)));
            return true;
        }
        return false;
    }
}