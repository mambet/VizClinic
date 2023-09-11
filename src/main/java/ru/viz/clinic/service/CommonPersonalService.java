package ru.viz.clinic.service;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import ru.viz.clinic.data.entity.Personal;
import ru.viz.clinic.repository.CommonPersonalRepository;
import ru.viz.clinic.security.AuthenticationService;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Getter
public abstract class CommonPersonalService<E extends Personal, R extends CommonPersonalRepository<E>> extends CommonEntityService<E,
        R> implements MessageGetter<E>, Cruad<E> {
    private final CustomTransactionalPersonalService<E, R> customTransactionalPersonalService;
    private final AuthenticationService authenticationService;
    private final OrderService orderService;

    public CommonPersonalService(
            @NotNull final R repository,
            @NotNull final CustomTransactionalPersonalService<E, R> customTransactionalPersonalService,
            @NotNull final OrderService orderService,
            @NotNull final AuthenticationService authenticationService

    ) {
        super(Objects.requireNonNull(repository));
        this.customTransactionalPersonalService = Objects.requireNonNull(customTransactionalPersonalService);
        this.authenticationService = Objects.requireNonNull(authenticationService);
        this.customTransactionalPersonalService.setRepository(repository);
        this.orderService = Objects.requireNonNull(orderService);
    }

    public Optional<E> getLoggedPersonal() {
        final Optional<UserDetails> userDetails = authenticationService.getUserDetails();
        if (userDetails.isPresent()) {
            final E byUsername = repository.findByUsername(Objects.requireNonNull(userDetails.get().getUsername()));
            if (byUsername != null) {
                return Optional.of(byUsername);
            }
        }
        log.info("No personal data found for the user.");
        return Optional.empty();
    }

    @Override
    public Optional<E> create(final E e) {
        try {
            final Optional<E> save = customTransactionalPersonalService.createWithAuthority(e);
            showCreateSuccessMessage(e);
            log.info("Personal data created successfully id: {}, name: {}", e.getId(), e.getEntityName());
            return save;
        } catch (final Exception exc) {
            showCreateErrMessage(e);
            log.error("Error creating personal data id: {}, name: {}", e.getId(), e.getEntityName(), exc);
        }
        return Optional.empty();
    }

    @Override
    public void delete(final E e) {
        Objects.requireNonNull(e);
        final Optional<E> optionalE = get(e.getId());
        if (optionalE.isEmpty()) {
            showEntityNotFoundErrorMessage(e);
            return;
        }
        if (isReadyToDelete(optionalE.get())) {
            try {
                customTransactionalPersonalService.deleteWithAuthority(e);
                showDeleteSuccessMessage(e);
                log.info("Personal data deleted successfully id: {}, name: {}", e.getId(), e.getEntityName());
            } catch (final Exception exc) {
                log.error("Error deleting personal data id: {}, name: {}", e.getId(), e.getEntityName(), exc);
                showDeleteErrMessage(e);
            }
        }
    }

    @Override
    protected void activate(final E e) {
        Objects.requireNonNull(e);
        final Optional<E> optionalE = get(e.getId());
        if (optionalE.isEmpty()) {
            showEntityNotFoundErrorMessage(e);
            return;
        }
        if (isReadyToActivate(optionalE.get())) {
            try {
                customTransactionalPersonalService.activateWithAuthority(e);
                showActivateSuccessMessage(e);
                log.info("Personal data activated successfully id: {}, name: {}", e.getId(), e.getEntityName());
            } catch (final Exception ex) {
                showActivateErrMessage(e);
                log.error("Error activating personal data id: {}, name: {}", e.getId(), e.getEntityName(), ex);
            }
        }
    }

    @Override
    protected void deactivate(final E e) {
        Objects.requireNonNull(e);
        final Optional<E> optionalE = get(e.getId());
        if (optionalE.isEmpty()) {
            showEntityNotFoundErrorMessage(e);
            return;
        }
        if (isReadyToDeactivate(optionalE.get())) {
            try {
                customTransactionalPersonalService.deactivateWithAuthority(e);
                showDeactivateSuccessMessage(e);
                log.info("Personal data deactivated successfully id: {}, name: {}", e.getId(), e.getEntityName());
            } catch (final Exception ex) {
                showDeactivateErrMessage(e);
                log.error("Error deactivating personal data id: {}, name: {}", e.getId(), e.getEntityName(), ex);
            }
        }
    }
}