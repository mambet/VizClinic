package ru.viz.clinic.service;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.viz.clinic.data.entity.AbstractEntity;
import ru.viz.clinic.repository.CommonRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

@Slf4j

public abstract class AbstractService<E extends AbstractEntity, R extends CommonRepository<E>> {
    protected final R repository;

    public AbstractService(final R repository) {
        this.repository = repository;
    }

    public Optional<E> get(final Long id) {
        return repository.findById(id);
    }

    public void delete(
            @NotNull final E e,
            @NotNull final Consumer<E> successHandler,
            @NotNull final Consumer<E> errorHandler
    ) {
        repository.delete(e);
        Objects.requireNonNull(e);
        Objects.requireNonNull(successHandler);
        Objects.requireNonNull(errorHandler);
        try {
            repository.delete(e);
            successHandler.accept(e);
        } catch (final Exception ex) {
            errorHandler.accept(e);
        }
    }

    public Page<E> list(final Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

    public List<E> getAllActive() {
        return repository.findByActiveTrue();
    }

    public List<E> getAllInactive() {
        return repository.findByActiveFalse();
    }

    public void saveAll(final Iterable<E> list) {
        repository.saveAll(list);
    }

    protected Optional<E> save(
            @NotNull final E e,
            @NotNull final Consumer<E> successHandler,
            @NotNull final Consumer<E> errorHandler
    ) {
        Objects.requireNonNull(e);
        Objects.requireNonNull(successHandler);
        Objects.requireNonNull(errorHandler);
        try {
            final E save = repository.save(e);
            successHandler.accept(save);
            return Optional.of(save);
        } catch (final Exception ex) {
            errorHandler.accept(e);
        }
        return Optional.empty();
    }
}