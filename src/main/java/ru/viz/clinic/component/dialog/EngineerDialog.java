package ru.viz.clinic.component.dialog;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.select.Select;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import ru.viz.clinic.data.entity.Engineer;
import ru.viz.clinic.data.entity.Hospital;
import ru.viz.clinic.data.model.EngineerPersonalDTO;
import ru.viz.clinic.data.repository.EngineerPersonalRepository;
import ru.viz.clinic.service.EngineerPersonalService;

import java.util.Collection;
import java.util.Objects;

public class EngineerDialog extends PersonalDialog<EngineerPersonalDTO, Engineer, EngineerPersonalRepository> {
    Select<Hospital> hospitalSelect = new Select<>();

    public EngineerDialog(
            @NotNull final EngineerPersonalService engineerPersonalService,
            @NotNull final Collection<Hospital> hospitals,
            @NotNull final EngineerPersonalDTO engineerPersonalDTO
    ) {
        super(engineerPersonalDTO, engineerPersonalService);

        hospitalSelect.setItems(hospitals);
        hospitalSelect.setItemLabelGenerator(Hospital::getName);

        binder.forField(hospitalSelect)
                .asRequired()
                .bind(EngineerPersonalDTO::getHospital, EngineerPersonalDTO::setHospital);

        addToFormLayout(hospitalSelect);
    }

    public EngineerDialog(
            @NotNull final EngineerPersonalService engineerPersonalService,
            @NotNull final Collection<Hospital> hospitals
    ) {
        this(engineerPersonalService, hospitals, EngineerPersonalDTO.builder().build());
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