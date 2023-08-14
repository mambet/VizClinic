package ru.viz.clinic.service;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.viz.clinic.data.entity.Engineer;
import ru.viz.clinic.data.repository.EngineerPersonalRepository;
import ru.viz.clinic.help.Helper;
import ru.viz.clinic.security.AuthenticationService;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static ru.viz.clinic.help.Translator.*;

@Service
@Slf4j
public class EngineerService extends AbstractService<Engineer, EngineerPersonalRepository> {
    private final AuthenticationService authenticationService;

    public EngineerService(
            @NotNull final EngineerPersonalRepository engineerPersonalRepository,
            @NotNull final AuthenticationService authenticationService
    ) {
        super(Objects.requireNonNull(engineerPersonalRepository));
        this.authenticationService = Objects.requireNonNull(authenticationService);
    }

    public Set<Engineer> getByHospitalId(@NotNull final Long hospitalId) {
        return repository.getByHospitalId(Objects.requireNonNull(hospitalId));
    }

    public Optional<Engineer> getLoggedEngineer() {
        final Optional<UserDetails> userDetails = authenticationService.getUserDetails();
        if (userDetails.isPresent()) {
            return findByUsername(userDetails.get().getUsername());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Engineer> save(final Engineer engineer) {
        try {
            final Optional<Engineer> optionalEngineer = super.save(engineer);
            Helper.showSuccessNotification(MSG_ENGINEER_SUCCESS_SAVED);
            return optionalEngineer;
        } catch (final Exception e) {
            log.error("error ", e);
            Helper.showErrorNotification(ERR_MSG_ENGINEER_SUCCESS_SAVED);
        }
        return Optional.empty();
    }
}
