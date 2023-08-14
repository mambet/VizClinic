package ru.viz.clinic.component.dialog;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import ru.viz.clinic.component.components.HospitalSelect;
import ru.viz.clinic.data.entity.Engineer;
import ru.viz.clinic.data.entity.Hospital;
import ru.viz.clinic.data.repository.EngineerPersonalRepository;
import ru.viz.clinic.service.EngineerService;

import java.util.Collection;
import java.util.Objects;

import static ru.viz.clinic.help.Translator.DLH_CREATE_ENGINEER;

public class EngineerDialog extends PersonalDialog<Engineer, EngineerPersonalRepository> {
    public EngineerDialog(
            @NotNull final Engineer engineer,
            @NotNull final Collection<Hospital> hospitals
    ) {
        super(engineer, DLH_CREATE_ENGINEER);

        final HospitalSelect hospitalSelect = new HospitalSelect(hospitals);

        binder.forField(hospitalSelect)
                .asRequired()
                .bind(Engineer::getHospital, Engineer::setHospital);

        formLayout.addComponentAtIndex(0, hospitalSelect);
        binder.readBean(engineer);
    }

    public EngineerDialog(
            @NotNull final EngineerService engineerService,
            @NotNull final Collection<Hospital> hospitals
    ) {
        this(new Engineer(), hospitals);
        addAuthorisationFields(engineerService);
    }

    @Override
    protected void handleConfirm() {
        if (createAuthority) {
            fireEvent(new CreateEngineerPersonalEvent(this, Objects.requireNonNull(item)));
        } else {
            fireEvent(new UpdateEngineerPersonalEvent(this, Objects.requireNonNull(item)));
        }
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
    public static class UpdateEngineerPersonalEvent extends AbstractDialogEvent<EngineerDialog, Engineer> {
        public UpdateEngineerPersonalEvent(
                final EngineerDialog source,
                final Engineer engineerPersonalDTO
        ) {
            super(Objects.requireNonNull(source), Objects.requireNonNull(engineerPersonalDTO));
        }
    }
}