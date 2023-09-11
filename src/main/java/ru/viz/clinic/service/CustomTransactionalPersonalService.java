package ru.viz.clinic.service;

import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.viz.clinic.data.entity.Personal;
import ru.viz.clinic.repository.CommonPersonalRepository;
import ru.viz.clinic.security.AuthenticationService;

import java.util.Objects;
import java.util.Optional;

@Service
public class CustomTransactionalPersonalService<E extends Personal, R extends CommonPersonalRepository<E>> {
    private final AuthenticationService authenticationService;
    R repository;

    public CustomTransactionalPersonalService(@NotNull final AuthenticationService authenticationService) {
        this.authenticationService = Objects.requireNonNull(authenticationService);
    }

    public void setRepository(final R repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    @Transactional
    public void deactivateWithAuthority(final E e) {
        Objects.requireNonNull(e);
        authenticationService.deleteUser(e.getUsername());
        e.setActive(false);
        repository.save(e);
    }

    @Transactional
    public void activateWithAuthority(final E e) {
        Objects.requireNonNull(e);
        authenticationService.createTempUserDetails(e.getUsername(), e.getTempPass());
        e.setActive(true);
        e.setTempPass(null);
        repository.save(e);
    }

    @Transactional
    public Optional<E> createWithAuthority(final E e) {
        Objects.requireNonNull(e);
        authenticationService.createTempUserDetails(e.getUsername(), e.getTempPass());
        e.setTempPass(null);
        return Optional.of(repository.save(e));
    }

    @Transactional
    public void deleteWithAuthority(final E e) {
        Objects.requireNonNull(e);
        repository.delete(e);
        authenticationService.deleteUser(e.getUsername());
    }
}