package ru.viz.clinic.component.dialog;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.select.Select;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import ru.viz.clinic.component.components.DepartmentSelect;
import ru.viz.clinic.component.components.HospitalSelect;
import ru.viz.clinic.data.entity.Hospital;
import ru.viz.clinic.data.entity.Medic;
import ru.viz.clinic.data.repository.MedicPersonalRepository;
import ru.viz.clinic.service.DepartmentService;
import ru.viz.clinic.service.MedicService;

import java.util.Collection;
import java.util.Objects;

import static ru.viz.clinic.help.Translator.DLH_CREATE_MEDIC;

public class MedicDialog extends PersonalDialog<Medic, MedicPersonalRepository> {
    private final DepartmentService departmentService;
    private final DepartmentSelect departmentSelect = new DepartmentSelect();

    public MedicDialog(
            @NotNull final Medic medic,
            @NotNull final Collection<Hospital> hospitals,
            @NotNull final DepartmentService departmentService
    ) {
        super(Objects.requireNonNull(medic), DLH_CREATE_MEDIC);
        this.departmentService = Objects.requireNonNull(departmentService);

        final HospitalSelect hospitalSelect = new HospitalSelect(hospitals);
        hospitalSelect.addValueChangeListener(this::hospitalSelectListener);

        binder.forField(hospitalSelect)
                .asRequired()
                .bind(m -> {
                    if (m.getDepartment() != null) {
                        return m.getDepartment().getHospital();
                    }
                    return null;
                }, (m, d) -> {
                });

        binder.forField(departmentSelect)
                .asRequired()
                .bind(Medic::getDepartment, Medic::setDepartment);

        formLayout.addComponentAtIndex(0, hospitalSelect);
        formLayout.addComponentAtIndex(1, departmentSelect);

        binder.readBean(medic);
    }

    public MedicDialog(
            @NotNull final MedicService medicService,
            @NotNull final Collection<Hospital> hospitals,
            @NotNull final DepartmentService departmentService
    ) {
        this(new Medic(), hospitals, departmentService);
        super.addAuthorisationFields(medicService);
    }

    private void hospitalSelectListener(final AbstractField.ComponentValueChangeEvent<Select<Hospital>, Hospital> selectHospitalComponentValueChangeEvent) {
        final Long hospitalId = selectHospitalComponentValueChangeEvent.getValue().getId();
        departmentSelect.setItems(departmentService.getByHospital(hospitalId));
    }

    @Override
    protected void handleConfirm() {
        if (createAuthority) {
            fireEvent(new CreateMedicPersonalEvent(this, Objects.requireNonNull(item)));
        } else {
            fireEvent(new UpdateMedicPersonalEvent(this, Objects.requireNonNull(item)));
        }
    }

    @Getter
    public static class CreateMedicPersonalEvent extends AbstractDialogEvent<MedicDialog, Medic> {
        public CreateMedicPersonalEvent(
                final MedicDialog source,
                final Medic medic
        ) {
            super(Objects.requireNonNull(source), Objects.requireNonNull(medic));
        }
    }

    @Getter
    public static class UpdateMedicPersonalEvent extends AbstractDialogEvent<MedicDialog, Medic> {
        public UpdateMedicPersonalEvent(
                final MedicDialog source,
                final Medic medic
        ) {
            super(Objects.requireNonNull(source), Objects.requireNonNull(medic));
        }
    }
}