package ru.viz.clinic.repository;

import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Repository;
import ru.viz.clinic.data.entity.Engineer;

import java.util.Set;

@Repository
public interface EngineerRepository extends CommonPersonalRepository<Engineer> {
    Set<Engineer> getByHospitalId(@NotNull final Long id);

    Set<Engineer> getByHospitalIdAndActiveIs(
            @NotNull final Long id,
            final boolean active
    );
}

