package ru.viz.clinic.service;

import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import ru.viz.clinic.data.entity.Admin;
import ru.viz.clinic.repository.AdministratorRepository;
import ru.viz.clinic.security.AuthenticationService;

import java.util.Objects;

@Service
public class AdministratorService extends CommonPersonalService<Admin, AdministratorRepository> {
    public AdministratorService(
            @NotNull final AdministratorRepository administratorRepository,
            @NotNull final CustomTransactionalPersonalService<Admin, AdministratorRepository> transactionalPersonalService,
            @NotNull final OrderService orderService,
            @NotNull final AuthenticationService authenticationService

    ) {
        super(Objects.requireNonNull(administratorRepository),
                Objects.requireNonNull(transactionalPersonalService),
                Objects.requireNonNull(orderService),
                Objects.requireNonNull(authenticationService));
    }

    @Override
    public boolean isReadyToActivate(final Admin admin) {
        return false;
    }

    @Override
    public boolean isReadyToDeactivate(final Admin admin) {
        return false;
    }

    @Override
    public boolean isReadyToDelete(final Admin admin) {
        return false;
    }

    @Override
    public void showUpdateSuccessMessage(final Admin admin) {

    }

    @Override
    public void showUpdateErrMessage(final Admin admin) {

    }

    @Override
    public void showCreateSuccessMessage(final Admin admin) {

    }

    @Override
    public void showCreateErrMessage(final Admin admin) {

    }

    @Override
    public void showDeleteSuccessMessage(final Admin admin) {

    }

    @Override
    public void showDeleteErrMessage(final Admin admin) {

    }

    @Override
    public void showActivateSuccessMessage(final Admin admin) {

    }

    @Override
    public void showActivateErrMessage(final Admin admin) {

    }

    @Override
    public void showDeactivateSuccessMessage(final Admin admin) {

    }

    @Override
    public void showDeactivateErrMessage(final Admin admin) {

    }
}
