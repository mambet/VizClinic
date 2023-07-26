package ru.viz.clinic.data.service;

import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.viz.clinic.data.entity.EngineerPersonal;
import ru.viz.clinic.data.entity.MedicPersonal;
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
        List<MedicPersonal> medicPersonalList = medicPersonalService.findByUsername(userDetails.getUsername());
        if (!medicPersonalList.isEmpty()) {
            return Optional.of(medicPersonalList.get(0));
        }
        List<EngineerPersonal> engineerPersonalList = engineerPersonalService.findByUsername(userDetails.getUsername());
        if (!engineerPersonalList.isEmpty()) {
            return Optional.of(engineerPersonalList.get(0));
        }
        return Optional.empty();
    }
}
