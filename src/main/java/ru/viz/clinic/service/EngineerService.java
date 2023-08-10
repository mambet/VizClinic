package ru.viz.clinic.service;

import jakarta.validation.constraints.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.viz.clinic.data.entity.Engineer;
import ru.viz.clinic.data.model.EngineerPersonalDTO;
import ru.viz.clinic.data.repository.EngineerPersonalRepository;
import ru.viz.clinic.security.AuthenticationService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class EngineerService extends AbstractService<Engineer, EngineerPersonalRepository, EngineerPersonalDTO> {
    private final AuthenticationService authenticationService;

    public EngineerService(
            @NotNull final EngineerPersonalRepository engineerPersonalRepository,
            @NotNull final ModelMapper modelMapper,
            @NotNull final AuthenticationService authenticationService
    ) {
        super(Engineer.class, Objects.requireNonNull(engineerPersonalRepository), Objects.requireNonNull(modelMapper));
        this.authenticationService = Objects.requireNonNull(authenticationService);
    }

    public Set<Engineer> getByHospitalId(@NotNull final Long hospitalId) {
        return repository.getByHospitalId(hospitalId);
    }

    public Optional<Engineer> getLoggedEngineer() {
        Optional<UserDetails> userDetails = authenticationService.getUserDetails();
        if (userDetails.isPresent()) {
            return findByUsername(userDetails.get().getUsername());
        }
        return Optional.empty();
    }
}
