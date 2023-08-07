package ru.viz.clinic.service;

import ru.viz.clinic.data.entity.Personal;

import java.util.Optional;

public interface CommonService <E extends Personal> {
    Optional<E> save(E personal);
}
