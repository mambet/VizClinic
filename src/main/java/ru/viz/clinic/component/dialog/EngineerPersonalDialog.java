package ru.viz.clinic.component.dialog;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.select.Select;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import ru.viz.clinic.data.entity.EngineerPersonal;
import ru.viz.clinic.data.entity.Hospital;
import ru.viz.clinic.data.model.EngineerPersonalDTO;
import ru.viz.clinic.data.repository.EngineerPersonalRepository;
import ru.viz.clinic.data.service.EngineerPersonalService;

import java.util.Collection;
import java.util.Objects;

public class EngineerPersonalDialog extends PersonalDialog<EngineerPersonalDTO, EngineerPersonal, EngineerPersonalRepository> {
    Select<Hospital> hospitalSelect = new Select<>();

    public EngineerPersonalDialog(
            @NotNull final EngineerPersonalService engineerPersonalService,
            @NotNull final Collection<Hospital> hospitals,
            @NotNull final EngineerPersonalDTO engineerPersonalDTO
    ) {
        super(engineerPersonalDTO, engineerPersonalService, EngineerPersonalDTO.class);

        hospitalSelect.setItems(hospitals);
        hospitalSelect.setItemLabelGenerator(Hospital::getName);

        binder.forField(hospitalSelect)
                .asRequired()
                .bind(EngineerPersonalDTO::getHospital, EngineerPersonalDTO::setHospital);

        addToFormLayout(hospitalSelect);
    }

    public EngineerPersonalDialog(
            @NotNull final EngineerPersonalService engineerPersonalService,
            @NotNull final Collection<Hospital> hospitals
    ) {
        this(engineerPersonalService, hospitals, EngineerPersonalDTO.builder().build());
    }

    @Override
    protected void firePersonalEvent(@NotNull final EngineerPersonalDTO engineerPersonalDTO) {
        fireEvent(new UpdateEngineerPersonalEvent(this, Objects.requireNonNull(engineerPersonalDTO)));
    }

    @Getter
    public static class UpdateEngineerPersonalEvent extends ComponentEvent<EngineerPersonalDialog> {
        private final EngineerPersonalDTO engineerPersonalDTO;

        public UpdateEngineerPersonalEvent(
                EngineerPersonalDialog source,
                EngineerPersonalDTO engineerPersonalDTO
        ) {
            super(source, true);
            this.engineerPersonalDTO = engineerPersonalDTO;
        }
    }
}