package ru.viz.clinic.service;

import jakarta.validation.constraints.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.viz.clinic.data.entity.Engineer;
import ru.viz.clinic.data.entity.Medic;
import ru.viz.clinic.data.model.MedicPersonalDTO;
import ru.viz.clinic.data.repository.MedicPersonalRepository;
import ru.viz.clinic.security.AuthenticationService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class MedicService extends AbstractService<Medic, MedicPersonalRepository, MedicPersonalDTO> {
    private final AuthenticationService authenticationService;

    public MedicService(
            @NotNull final MedicPersonalRepository medicPersonalRepository,
            @NotNull final ModelMapper modelMapper,
            @NotNull final AuthenticationService authenticationService
    ) {
        super(Medic.class,
                Objects.requireNonNull(medicPersonalRepository),
                Objects.requireNonNull(modelMapper));
        this.authenticationService = Objects.requireNonNull(authenticationService);
    }

    public Optional<Medic> getLoggedMedic() {
        Optional<UserDetails> userDetails = authenticationService.getUserDetails();
        if (userDetails.isPresent()) {
            return findByUsername(userDetails.get().getUsername());
        }
        return Optional.empty();
    }
}

