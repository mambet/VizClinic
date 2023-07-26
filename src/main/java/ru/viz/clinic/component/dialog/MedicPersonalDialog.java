package ru.viz.clinic.component.dialog;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.select.Select;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import ru.viz.clinic.data.entity.Department;
import ru.viz.clinic.data.entity.EngineerPersonal;
import ru.viz.clinic.data.entity.Hospital;
import ru.viz.clinic.data.entity.MedicPersonal;
import ru.viz.clinic.data.model.EngineerPersonalDTO;
import ru.viz.clinic.data.model.MedicPersonalDTO;
import ru.viz.clinic.data.repository.EngineerPersonalRepository;
import ru.viz.clinic.data.repository.MedicPersonalRepository;
import ru.viz.clinic.data.service.MedicPersonalService;

import java.util.Collection;
import java.util.Objects;

public class MedicPersonalDialog extends  PersonalDialog<MedicPersonalDTO, MedicPersonal, MedicPersonalRepository> {
    Select<Hospital> hospitalSelect = new Select<>();
    Select<Department> hospitalDepartmentSelect = new Select<>();

    public MedicPersonalDialog(
            @NotNull final MedicPersonalService medicPersonalService,
            @NotNull final Collection<Hospital> hospitals,
            @NotNull final MedicPersonalDTO medicPersonalDTO
    ) {
        super(medicPersonalDTO, medicPersonalService, MedicPersonalDTO.class);

        hospitalSelect.addValueChangeListener(this::hospitalSelectListener);
        hospitalSelect.setItems(hospitals);
        hospitalSelect.setItemLabelGenerator(Hospital::getName);

        hospitalDepartmentSelect.setItemLabelGenerator(Department::getName);

        binder.forField(hospitalDepartmentSelect)
                .asRequired()
                .bind(MedicPersonalDTO::getDepartment, MedicPersonalDTO::setDepartment);

        addToFormLayout(hospitalSelect, hospitalDepartmentSelect);
    }

    public MedicPersonalDialog(
            @NotNull final MedicPersonalService medicPersonalService,
            @NotNull final Collection<Hospital> hospitals
    ) {
        this(medicPersonalService, hospitals, MedicPersonalDTO.builder().build());
    }

    private void hospitalSelectListener(AbstractField.ComponentValueChangeEvent<Select<Hospital>, Hospital> selectHospitalComponentValueChangeEvent) {
        hospitalDepartmentSelect.setItems(selectHospitalComponentValueChangeEvent.getValue().getDepartments());
    }

    @Getter
    public static class UpdateMedicPersonalEvent extends ComponentEvent<MedicPersonalDialog> {
        private final MedicPersonalDTO medicPersonalDTO;

        public UpdateMedicPersonalEvent(
                MedicPersonalDialog source,
                MedicPersonalDTO medicPersonalDTO
        ) {
            super(source, true);
            this.medicPersonalDTO = medicPersonalDTO;
        }
    }

    @Override
    protected void firePersonalEvent(@NotNull final MedicPersonalDTO medicPersonalDTO) {
        fireEvent(new UpdateMedicPersonalEvent(this, Objects.requireNonNull(medicPersonalDTO)));
    }
}
