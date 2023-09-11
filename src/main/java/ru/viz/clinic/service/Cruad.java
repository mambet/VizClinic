package ru.viz.clinic.service;

import ru.viz.clinic.data.entity.AbstractEntity;

import java.util.Optional;

public interface Cruad<E extends AbstractEntity> {
    Optional<E> create(E e);

    Optional<E> update(E e);

    void setActive(
            E e,
            boolean active
    );

    void delete(E e);
}
