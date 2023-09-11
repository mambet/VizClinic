package ru.viz.clinic.component.dialog;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import ru.viz.clinic.component.components.HospitalSelect;
import ru.viz.clinic.data.entity.Engineer;
import ru.viz.clinic.data.entity.Hospital;
import ru.viz.clinic.repository.EngineerRepository;
import ru.viz.clinic.security.AuthenticationService;

import java.util.Collection;
import java.util.Objects;

import static ru.viz.clinic.help.Translator.DLH_CREATE_ENGINEER;

public class EngineerDialog extends PersonalDialog<Engineer, EngineerRepository> {
    private EngineerDialog(
            @NotNull final Engineer engineer
    ) {
        super(engineer, DLH_CREATE_ENGINEER);
    }

    public static EngineerDialog getUpdateDialog(
            @NotNull final Engineer engineer,
            @NotNull final Collection<Hospital> hospitals
    ) {
        final EngineerDialog engineerDialog = new EngineerDialog(engineer);
        engineerDialog.addHospitalSelect(hospitals);
        engineerDialog.addPersonalFields();
        engineerDialog.initUpdate();
        engineerDialog.binder.readBean(engineer);
        return engineerDialog;
    }

    public static EngineerDialog getCreateDialog(
            @NotNull final AuthenticationService authenticationService,
            @NotNull final Collection<Hospital> hospitals
    ) {
        final EngineerDialog engineerDialog = new EngineerDialog(new Engineer());
        engineerDialog.addAuthorisationFields(authenticationService);
        engineerDialog.addHospitalSelect(hospitals);
        engineerDialog.addPersonalFields();
        engineerDialog.initCreate();
        return engineerDialog;
    }

    public static EngineerDialog getUpdateAuthorityDialog(
            @NotNull final Engineer engineer,
            @NotNull final AuthenticationService authenticationService
    ) {
        final EngineerDialog engineerDialog = new EngineerDialog(engineer);
        engineerDialog.addAuthorisationFields(authenticationService);
        engineerDialog.binder.readBean(engineer);
        engineerDialog.initUpdate();
        return engineerDialog;
    }

    private void addHospitalSelect(Collection<Hospital> hospitals) {
        final HospitalSelect hospitalSelect = HospitalSelect.createHospitalSelect(hospitals);
        binder.forField(hospitalSelect)
                .asRequired()
                .bind(Engineer::getHospital, Engineer::setHospital);

        formLayout.add(hospitalSelect);
    }

    @Getter
    public static class CreateEngineerPersonalEvent extends AbstractDialogEvent<EngineerDialog, Engineer> {
        public CreateEngineerPersonalEvent(
                final EngineerDialog source,
                final Engineer engineerPersonalDTO
        ) {
            super(Objects.requireNonNull(source), Objects.requireNonNull(engineerPersonalDTO));
        }
    }

    @Getter
    public static class UpdateEngineerDialogEvent extends AbstractDialogEvent<EngineerDialog, Engineer> {
        public UpdateEngineerDialogEvent(
                final EngineerDialog source,
                final Engineer engineerPersonalDTO
        ) {
            super(Objects.requireNonNull(source), Objects.requireNonNull(engineerPersonalDTO));
        }
    }

    @Override
    protected void handleCreate() {
        fireEvent(new CreateEngineerPersonalEvent(this, Objects.requireNonNull(item)));
    }

    @Override
    protected void handleUpdate() {
        fireEvent(new UpdateEngineerDialogEvent(this, Objects.requireNonNull(item)));
    }
}