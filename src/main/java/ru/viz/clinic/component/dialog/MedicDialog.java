package ru.viz.clinic.component.dialog;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.select.Select;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import ru.viz.clinic.component.components.DepartmentSelect;
import ru.viz.clinic.component.components.HospitalSelect;
import ru.viz.clinic.data.entity.Hospital;
import ru.viz.clinic.data.entity.Medic;
import ru.viz.clinic.repository.MedicRepository;
import ru.viz.clinic.security.AuthenticationService;
import ru.viz.clinic.service.DepartmentService;
import ru.viz.clinic.service.MedicService;

import java.util.Collection;
import java.util.Objects;

import static ru.viz.clinic.help.Translator.DLH_CREATE_MEDIC;

public class MedicDialog extends PersonalDialog<Medic, MedicRepository> {
    protected  DepartmentService departmentService;
    private final DepartmentSelect departmentSelect = DepartmentSelect.createDepartmentSelect();

    private MedicDialog(
            @NotNull final Medic medic
    ) {
        super(Objects.requireNonNull(medic), DLH_CREATE_MEDIC);

        binder.readBean(medic);
    }

    public static MedicDialog getUpdateDialog(
            @NotNull final Medic medic,
            @NotNull final Collection<Hospital> hospitals,
            @NotNull final DepartmentService departmentService
    ) {
        final MedicDialog medicDialog = new MedicDialog(medic);
        medicDialog.departmentService = Objects.requireNonNull(departmentService);
        medicDialog.addHospitalSelect(hospitals);
        medicDialog.addDepartmentSelect();
        medicDialog.addPersonalFields();
        medicDialog.binder.readBean(medic);
        medicDialog.initUpdate();
        return medicDialog;
    }

    public static MedicDialog getCreateDialog(
            @NotNull final Collection<Hospital> hospitals,
            @NotNull final DepartmentService departmentService,
            @NotNull final AuthenticationService authenticationService
    ) {
        final MedicDialog medicDialog = new MedicDialog(new Medic());
        medicDialog.departmentService = Objects.requireNonNull(departmentService);
        medicDialog.addAuthorisationFields(authenticationService);
        medicDialog.addHospitalSelect(hospitals);
        medicDialog.addDepartmentSelect();
        medicDialog.addPersonalFields();
        medicDialog.initCreate();
        return medicDialog;
    }

    public static MedicDialog getUpdateAuthorityDialog(
            @NotNull final Medic medic,
            @NotNull final AuthenticationService authenticationService
    ) {
        final MedicDialog medicDialog = new MedicDialog(medic);
        medicDialog.addAuthorisationFields(authenticationService);
        medicDialog.binder.readBean(medic);
        medicDialog.initUpdate();
        return medicDialog;
    }

    private void addHospitalSelect(Collection<Hospital> hospitals) {
        final HospitalSelect hospitalSelect = HospitalSelect.createHospitalSelect(hospitals);
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

        formLayout.add(hospitalSelect);
    }

    private void addDepartmentSelect() {
        binder.forField(departmentSelect)
                .asRequired()
                .bind(Medic::getDepartment, Medic::setDepartment);
        formLayout.add(departmentSelect);
    }

    private void hospitalSelectListener(final AbstractField.ComponentValueChangeEvent<Select<Hospital>, Hospital> selectHospitalComponentValueChangeEvent) {
        final Long hospitalId = selectHospitalComponentValueChangeEvent.getValue().getId();
        if (departmentSelect != null) {
            departmentSelect.setItems(departmentService.getActiveByHospitalId(hospitalId));
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
    public static class UpdateMedicEvent extends AbstractDialogEvent<MedicDialog, Medic> {
        public UpdateMedicEvent(
                final MedicDialog source,
                final Medic medic
        ) {
            super(Objects.requireNonNull(source), Objects.requireNonNull(medic));
        }
    }

    @Override
    protected void handleCreate() {
        fireEvent(new CreateMedicPersonalEvent(this, Objects.requireNonNull(item)));
    }

    @Override
    protected void handleUpdate() {
        fireEvent(new UpdateMedicEvent(this, Objects.requireNonNull(item)));
    }
}