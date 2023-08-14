package ru.viz.clinic.service;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.viz.clinic.data.entity.Medic;
import ru.viz.clinic.data.repository.MedicPersonalRepository;
import ru.viz.clinic.help.Helper;
import ru.viz.clinic.security.AuthenticationService;

import java.util.Objects;
import java.util.Optional;

import static ru.viz.clinic.help.Translator.*;

@Service
@Slf4j
public class MedicService extends AbstractService<Medic, MedicPersonalRepository> {
    private final AuthenticationService authenticationService;

    public MedicService(
            @NotNull final MedicPersonalRepository medicPersonalRepository,
            @NotNull final AuthenticationService authenticationService
    ) {
        super(Objects.requireNonNull(medicPersonalRepository));
        this.authenticationService = Objects.requireNonNull(authenticationService);
    }

    public Optional<Medic> getLoggedMedic() {
        final Optional<UserDetails> userDetails = authenticationService.getUserDetails();
        if (userDetails.isPresent()) {
            return findByUsername(userDetails.get().getUsername());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Medic> save(final Medic medic) {
        try {
            final Optional<Medic> save = super.save(medic);
            Helper.showSuccessNotification(MSG_MEDIC_SUCCESS_SAVED);
            return save;
        } catch (final Exception e) {
            log.error("error ", e);
            Helper.showErrorNotification(ERR_MSG_MEDIC_SUCCESS_SAVED);
        }
        return Optional.empty();
    }
}

