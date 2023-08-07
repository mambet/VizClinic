package ru.viz.clinic.service;

import jakarta.validation.constraints.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.viz.clinic.data.entity.Medic;
import ru.viz.clinic.data.model.MedicPersonalDTO;
import ru.viz.clinic.data.repository.MedicPersonalRepository;

import java.util.Objects;

@Service
public class MedicPersonalService extends AbstractService<Medic, MedicPersonalRepository, MedicPersonalDTO> {
    public MedicPersonalService(
            @NotNull final MedicPersonalRepository medicPersonalRepository,
            @NotNull final ModelMapper modelMapper
    ) {
        super(Medic.class, Objects.requireNonNull(medicPersonalRepository),
                Objects.requireNonNull(modelMapper));
    }
}

