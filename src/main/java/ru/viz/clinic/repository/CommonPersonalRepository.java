package ru.viz.clinic.repository;

import org.springframework.data.repository.NoRepositoryBean;
import ru.viz.clinic.data.entity.Personal;

@NoRepositoryBean
public interface CommonPersonalRepository<E extends Personal> extends CommonRepository<E> {
    E findByUsername(String username);
}