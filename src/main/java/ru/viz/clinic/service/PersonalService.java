package ru.viz.clinic.service;

import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.viz.clinic.data.entity.Engineer;
import ru.viz.clinic.data.entity.Medic;
import ru.viz.clinic.data.entity.Personal;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PersonalService {
    private final MedicPersonalService medicPersonalService;
    private final EngineerPersonalService engineerPersonalService;

    public PersonalService(
            @NotNull final MedicPersonalService medicPersonalService,
            @NotNull final EngineerPersonalService engineerPersonalService

    ) {
        this.medicPersonalService = Objects.requireNonNull(medicPersonalService);
        this.engineerPersonalService = Objects.requireNonNull(engineerPersonalService);
    }

    public Optional<Personal> getLoggedPersonal(@NotNull final UserDetails userDetails) {
        Objects.requireNonNull(userDetails);
        List<Medic> medicList = medicPersonalService.findByUsername(userDetails.getUsername());
        if (!medicList.isEmpty()) {
            return Optional.of(medicList.get(0));
        }
        List<Engineer> engineerList = engineerPersonalService.findByUsername(userDetails.getUsername());
        if (!engineerList.isEmpty()) {
            return Optional.of(engineerList.get(0));
        }
        return Optional.empty();
    }
}
