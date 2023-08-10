package ru.viz.clinic.service;

import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.viz.clinic.component.dialog.PassChangeLayout;
import ru.viz.clinic.data.Role;
import ru.viz.clinic.data.entity.Engineer;
import ru.viz.clinic.data.entity.Medic;
import ru.viz.clinic.security.AuthenticationService;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class PersonalService {
    private final MedicService medicService;
    private final EngineerService engineerService;
    private final AuthenticationService authenticationService;

    public PersonalService(
            @NotNull final MedicService medicService,
            @NotNull final EngineerService engineerService,
            @NotNull final AuthenticationService authenticationService

    ) {
        this.medicService = Objects.requireNonNull(medicService);
        this.engineerService = Objects.requireNonNull(engineerService);
        this.authenticationService = Objects.requireNonNull(authenticationService);
    }

    @Transactional
    public Optional<Role> updatePassAndRole(@NotNull final PassChangeLayout.PassDTO passDTO) {
        Objects.requireNonNull(passDTO);
        final String currentPass = Objects.requireNonNull(passDTO.getOldPass());
        final String newPass = Objects.requireNonNull(passDTO.getNewPass());

        Role role = null;
        String username = null;

        Optional<Medic> medicOptional = medicService.getLoggedMedic();
        Optional<Engineer> engineerOptional = engineerService.getLoggedEngineer();

        if (medicOptional.isPresent()) {
            role = Role.MEDIC;
            username = medicOptional.get().getUsername();
        } else if (engineerOptional.isPresent()) {
            role = Role.ENGINEER;
            username = engineerOptional.get().getUsername();
        }
        if (role != null && username != null) {
            authenticationService.updatePassAndRole(username, Set.of(role), currentPass, newPass);
            return Optional.of(role);
        }
        return Optional.empty();
    }
}
