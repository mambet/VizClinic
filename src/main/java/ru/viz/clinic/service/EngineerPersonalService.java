package ru.viz.clinic.service;

import jakarta.validation.constraints.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.viz.clinic.data.entity.Engineer;
import ru.viz.clinic.data.model.EngineerPersonalDTO;
import ru.viz.clinic.data.repository.EngineerPersonalRepository;

import java.util.Objects;

@Service
public class EngineerPersonalService extends AbstractService<Engineer, EngineerPersonalRepository, EngineerPersonalDTO> {
    public EngineerPersonalService(
            @NotNull final EngineerPersonalRepository engineerPersonalRepository,
            @NotNull final ModelMapper modelMapper
    ) {
        super(Engineer.class, Objects.requireNonNull(engineerPersonalRepository),
                Objects.requireNonNull(modelMapper));
    }
}
