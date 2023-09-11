package ru.viz.clinic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import ru.viz.clinic.data.entity.AbstractEntity;

import java.util.List;

@NoRepositoryBean
public interface CommonRepository<E extends AbstractEntity> extends JpaRepository<E, Long> {
    List<E> findByActiveTrue();

    List<E> findByActiveFalse();
}