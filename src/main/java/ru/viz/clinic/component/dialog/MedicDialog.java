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
import ru.viz.clinic.service.MedicPersonalService;

import java.util.Collection;
import java.util.Objects;

import static ru.viz.clinic.help.Translator.LBL_DEPARTMENT_NAME;

public class MedicDialog extends PersonalDialog<MedicPersonalDTO, Medic, MedicPersonalRepository> {
    Select<Department> departmentSelect = new Select<>();

    public MedicDialog(
            @NotNull final MedicPersonalService medicPersonalService,
            @NotNull final Collection<Hospital> hospitals,
            @NotNull final MedicPersonalDTO medicPersonalDTO
    ) {
        super(medicPersonalDTO, medicPersonalService);

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
            @NotNull final MedicPersonalService medicPersonalService,
            @NotNull final Collection<Hospital> hospitals
    ) {
        this(medicPersonalService, hospitals, MedicPersonalDTO.builder().build());
    }

    private void hospitalSelectListener(AbstractField.ComponentValueChangeEvent<Select<Hospital>, Hospital> selectHospitalComponentValueChangeEvent) {
        departmentSelect.setItems(selectHospitalComponentValueChangeEvent.getValue().getDepartments());
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