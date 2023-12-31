package ru.viz.clinic.service;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import ru.viz.clinic.component.dialog.PassChangeLayout;
import ru.viz.clinic.data.Role;
import ru.viz.clinic.data.entity.Admin;
import ru.viz.clinic.data.entity.Engineer;
import ru.viz.clinic.data.entity.Medic;
import ru.viz.clinic.help.Helper;
import ru.viz.clinic.security.AuthenticationService;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static ru.viz.clinic.help.Translator.ERR_AUTHENTICATION;
import static ru.viz.clinic.help.Translator.ERR_BAD_CREDENTIALS;

@Service
@Slf4j
public class ChangePassService {
    private final MedicService medicService;
    private final EngineerService engineerService;
    private final AdministratorService administratorService;
    private final AuthenticationService authenticationService;

    public ChangePassService(
            @NotNull final MedicService medicService,
            @NotNull final EngineerService engineerService,
            @NotNull final AdministratorService administratorService,
            @NotNull final AuthenticationService authenticationService

    ) {
        this.medicService = Objects.requireNonNull(medicService);
        this.engineerService = Objects.requireNonNull(engineerService);
        this.administratorService = Objects.requireNonNull(administratorService);
        this.authenticationService = Objects.requireNonNull(authenticationService);
    }

    public Optional<Role> updatePassAndRole(@NotNull final PassChangeLayout.PassDTO passDTO) {
        Objects.requireNonNull(passDTO);
        final String currentPass = Objects.requireNonNull(passDTO.getOldPass());
        final String newPass = Objects.requireNonNull(passDTO.getNewPass());

        Role role = null;
        String username = null;

        final Optional<Medic> medicOptional = medicService.getLoggedPersonal();
        final Optional<Engineer> engineerOptional = engineerService.getLoggedPersonal();
        final Optional<Admin> adminOptional = administratorService.getLoggedPersonal();

        if (medicOptional.isPresent()) {
            role = Role.MEDIC;
            username = medicOptional.get().getUsername();
        } else if (engineerOptional.isPresent()) {
            role = Role.ENGINEER;
            username = engineerOptional.get().getUsername();
        } else if (adminOptional.isPresent()) {
            role = Role.ADMIN;
            username = adminOptional.get().getUsername();
        }
        if (role != null && username != null) {
            try {
                authenticationService.updatePassAndRole(username, Set.of(role), currentPass, newPass);
                return Optional.of(role);
            } catch (final BadCredentialsException e) {
                Helper.showErrorNotification(ERR_BAD_CREDENTIALS);
                log.error("invalid pass", e);
            } catch (final Exception e) {
                Helper.showErrorNotification(ERR_AUTHENTICATION);
                log.error("error at update pass and role", e);
            }
        }
        return Optional.empty();
    }
}
