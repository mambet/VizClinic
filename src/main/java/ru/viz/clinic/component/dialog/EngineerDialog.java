package ru.viz.clinic.component.dialog;

import com.vaadin.flow.component.ComponentEvent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import ru.viz.clinic.component.components.HospitalSelect;
import ru.viz.clinic.data.entity.Engineer;
import ru.viz.clinic.data.entity.Hospital;
import ru.viz.clinic.data.model.EngineerPersonalDTO;
import ru.viz.clinic.data.repository.EngineerPersonalRepository;
import ru.viz.clinic.service.EngineerService;

import java.util.Collection;
import java.util.Objects;

import static ru.viz.clinic.help.Translator.DLH_CREATE_ENGINEER;

public class EngineerDialog extends PersonalDialog<EngineerPersonalDTO, Engineer, EngineerPersonalRepository> {
    public EngineerDialog(
            @NotNull final EngineerService engineerService,
            @NotNull final Collection<Hospital> hospitals,
            @NotNull final EngineerPersonalDTO engineerPersonalDTO
    ) {
        super(engineerPersonalDTO, engineerService, DLH_CREATE_ENGINEER);

        final HospitalSelect hospitalSelect = new HospitalSelect(hospitals);

        binder.forField(hospitalSelect)
                .asRequired()
                .bind(EngineerPersonalDTO::getHospital, EngineerPersonalDTO::setHospital);

        addComponents(hospitalSelect);
    }

    public EngineerDialog(
            @NotNull final EngineerService engineerService,
            @NotNull final Collection<Hospital> hospitals
    ) {
        this(engineerService, hospitals, EngineerPersonalDTO.builder().build());
    }

    @Override
    protected void handleConfirm() {
        fireEvent(new UpdateEngineerPersonalEvent(this, Objects.requireNonNull(item)));
    }

    @Getter
    public static class UpdateEngineerPersonalEvent extends ComponentEvent<EngineerDialog> {
        private final EngineerPersonalDTO engineerPersonalDTO;

        public UpdateEngineerPersonalEvent(
                EngineerDialog source,
                EngineerPersonalDTO engineerPersonalDTO
        ) {
            super(source, true);
            this.engineerPersonalDTO = engineerPersonalDTO;
        }
    }
}