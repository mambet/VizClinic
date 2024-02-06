package ru.viz.clinic.repository;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.viz.clinic.data.entity.Hospital;
import ru.viz.clinic.data.entity.Medic;

import java.util.Set;

@Repository
public interface MedicRepository extends CommonPersonalRepository<Medic>, JpaSpecificationExecutor<Medic> {
    Set<Medic> getByDepartmentId(@NotNull final String departmentId);

    Set<Medic> getByDepartmentIdAndActiveIs(
            @NotNull final String departmentId,
            final boolean active
    );
}