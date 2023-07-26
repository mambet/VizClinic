package ru.viz.clinic.data.service;

import ru.viz.clinic.data.entity.EngineerPersonal;
import ru.viz.clinic.data.entity.Personal;

import java.util.List;
import java.util.Optional;

public interface CommonService <E extends Personal> {
    Optional<E> save(E personal);
}
