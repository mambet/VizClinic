package ru.viz.clinic.component.dialog;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.select.Select;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import ru.viz.clinic.data.entity.Department;
import ru.viz.clinic.data.entity.Hospital;
import ru.viz.clinic.data.entity.Medic;
import ru.viz.clinic.data.model.MedicPersonalDTO;
import ru.viz.clinic.data.repository.MedicPersonalRepository;
import ru.viz.clinic.service.DepartmentService;
import ru.viz.clinic.service.MedicService;

import java.util.Collection;
import java.util.Objects;

import static ru.viz.clinic.help.Translator.LBL_DEPARTMENT_NAME;

public class MedicDialog extends PersonalDialog<MedicPersonalDTO, Medic, MedicPersonalRepository> {
    private final DepartmentService departmentService;
    Select<Department> departmentSelect = new Select<>();

    public MedicDialog(
            @NotNull final MedicService medicService,
            @NotNull final Collection<Hospital> hospitals,
            @NotNull final MedicPersonalDTO medicPersonalDTO,
            @NotNull final DepartmentService departmentService
    ) {
        super(medicPersonalDTO, medicService);
        this.departmentService = Objects.requireNonNull(departmentService);

        Select<Hospital> hospitalSelect = new Select<>();

        hospitalSelect.addValueChangeListener(this::hospitalSelectListener);
        hospitalSelect.setItems(hospitals);
        hospitalSelect.setItemLabelGenerator(Hospital::getName);

        departmentSelect.setLabel(LBL_DEPARTMENT_NAME);
        departmentSelect.setItemLabelGenerator(Department::getName);

        binder.forField(departmentSelect)
                .asRequired()
                .bind(MedicPersonalDTO::getDepartment, MedicPersonalDTO::setDepartment);

        addToFormLayout(hospitalSelect, departmentSelect);
    }

    public MedicDialog(
            @NotNull final MedicService medicService,
            @NotNull final Collection<Hospital> hospitals,
            @NotNull final DepartmentService departmentService
    ) {
        this(medicService, hospitals, MedicPersonalDTO.builder().build(),
                Objects.requireNonNull(departmentService));
    }

    private void hospitalSelectListener(AbstractField.ComponentValueChangeEvent<Select<Hospital>, Hospital> selectHospitalComponentValueChangeEvent) {
        final Long hospitalId = selectHospitalComponentValueChangeEvent.getValue().getId();
        departmentSelect.setItems(departmentService.getByHospital(hospitalId));
    }

    @Override
    protected void handleConfirm() {
        fireEvent(new UpdateMedicPersonalEvent(this, Objects.requireNonNull(item)));
    }

    @Getter
    public static class UpdateMedicPersonalEvent extends ComponentEvent<MedicDialog> {
        private final MedicPersonalDTO medicPersonalDTO;

        public UpdateMedicPersonalEvent(
                MedicDialog source,
                MedicPersonalDTO medicPersonalDTO
        ) {
            super(source, true);
            this.medicPersonalDTO = medicPersonalDTO;
        }
    }
}