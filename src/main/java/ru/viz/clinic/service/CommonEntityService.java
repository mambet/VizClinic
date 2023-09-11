package ru.viz.clinic.service;

import lombok.extern.slf4j.Slf4j;
import ru.viz.clinic.data.entity.AbstractEntity;
import ru.viz.clinic.data.entity.Order;
import ru.viz.clinic.help.Helper;
import ru.viz.clinic.repository.CommonRepository;

import java.util.Optional;

@Slf4j
public abstract class CommonEntityService<E extends AbstractEntity, R extends CommonRepository<E>> extends AbstractService<E, R> implements MessageGetter<E>, Cruad<E> {
    public CommonEntityService(final R repository) {
        super(repository);
    }

    public Optional<E> create(final E e) {
        return save(e, this::showCreateSuccessMessage, this::showCreateErrMessage);
    }

    public Optional<E> update(final E e) {
        return save(e, this::showUpdateSuccessMessage, this::showUpdateErrMessage);
    }

    public void delete(final E e) {
        final Optional<E> optionalE = get(e.getId());
        if (optionalE.isEmpty()) {
            showEntityNotFoundErrorMessage(e);
            return;
        }
        if (isReadyToDelete(optionalE.get())) {
            delete(e, this::showDeleteSuccessMessage, this::showDeleteErrMessage);
        }
    }

    public void setActive(
            final E e,
            final boolean active
    ) {
        if (active) {
            activate(e);
        } else {
            deactivate(e);
        }
    }

    protected void activate(final E e) {
        final Optional<E> optionalE = get(e.getId());
        if (optionalE.isEmpty()) {
            showEntityNotFoundErrorMessage(e);
            return;
        }
        if (isReadyToActivate(optionalE.get())) {
            e.setActive(true);
            save(e, this::showActivateSuccessMessage, this::showActivateErrMessage);
        }
    }

    protected void deactivate(final E e) {
        final Optional<E> optionalE = get(e.getId());
        if (optionalE.isEmpty()) {
            showEntityNotFoundErrorMessage(e);
            return;
        }
        if (isReadyToDeactivate(optionalE.get())) {
            e.setActive(false);
            save(e, this::showDeactivateSuccessMessage, this::showDeactivateErrMessage);
        }
    }

    public void showEntityNotFoundErrorMessage(final E e) {
        Helper.formatAndShowErrorMessage("Запись '%s' не найдена базе данных", e);
    }
}