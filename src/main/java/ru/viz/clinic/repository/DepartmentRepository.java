package ru.viz.clinic.repository;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.viz.clinic.data.entity.Department;

import java.util.List;

@Repository
public interface DepartmentRepository extends CommonRepository<Department>, JpaSpecificationExecutor<Department> {
    List<Department> getByHospitalId(@NotNull final Long hospitalId);

    List<Department> getByHospitalIdAndActiveIs(
            @NotNull final Long id,
            final boolean active
    );
}