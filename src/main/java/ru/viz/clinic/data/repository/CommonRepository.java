package ru.viz.clinic.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import ru.viz.clinic.data.entity.Personal;

import java.util.List;

@NoRepositoryBean
public interface CommonRepository<E extends Personal> extends JpaRepository<E, Long> {
    E findByUsername(String username);
}