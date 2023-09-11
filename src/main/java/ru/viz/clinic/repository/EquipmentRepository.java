package ru.viz.clinic.repository;

import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Repository;
import ru.viz.clinic.data.entity.Equipment;

import java.util.List;
import java.util.Set;

@Repository
public interface EquipmentRepository extends CommonRepository<Equipment> {

    Set<Equipment> getByDepartmentIdAndActiveIs(
            @NotNull final Long departmentId,
            final boolean active
    );
}
