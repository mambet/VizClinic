package ru.viz.clinic.data.repository;

import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Repository;
import ru.viz.clinic.data.entity.Engineer;

import java.util.List;
import java.util.Set;

@Repository
public interface EngineerPersonalRepository extends CommonRepository<Engineer> {
    @Override
    Engineer findByUsername(@NotNull final String username);

    Set<Engineer> getByHospitalId(@NotNull final Long id);
}

