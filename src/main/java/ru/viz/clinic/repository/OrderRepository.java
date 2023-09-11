package ru.viz.clinic.repository;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.viz.clinic.data.OrderState;
import ru.viz.clinic.data.entity.Order;

import java.util.List;
import java.util.Set;

@Repository
public interface OrderRepository extends CommonRepository<Order> {
    @Query("SELECT o FROM Order o WHERE o.equipment.department.hospital.id = :hospitalId")
    List<Order> getByHospitalId(@Param("hospitalId") Long hospitalId);

    Set<Order> getByEquipmentId(@NotNull final Long equipmentId);

    @Query("SELECT o FROM Order o WHERE o.equipment.department.id = :departmentId")
    List<Order> getByDepartmentId(@Param("departmentId") @NotNull final Long id);

    List<Order> getByDestinationEngineersId(@Param("engineersId") @NotNull final Long id);

    @Query("SELECT o FROM Order o WHERE o.equipment.department.hospital.id = :hospitalId AND o.active = :active")
    List<Order> getByHospitalIdAndActiveIs(
            @Param("hospitalId") @NotNull Long hospitalId,
            @Param("active") final boolean active
    );

    @Query("SELECT o FROM Order o WHERE o.equipment.department.hospital.id = :hospitalId AND o.active = :active AND  " +
            "o.orderState = :orderState")
    List<Order> getByHospitalIdAndActiveIsAndOrderStateIs(
            @Param("hospitalId") @NotNull Long hospitalId,
            @Param("active") final boolean active,
            @Param("orderState") final OrderState orderState
    );

    @Query("SELECT o FROM Order o WHERE o.equipment.department.id = :departmentId AND o.active = :active")
    List<Order> getByDepartmentIdAndActiveIs(
            @Param("departmentId") @NotNull final Long id,
            @Param("active") final boolean active
    );

    List<Order> getByDestinationEngineersIdAndActiveIs(
            @Param("engineersId") @NotNull final Long id,
            final boolean active
    );

    Set<Order> getByEquipmentIdAndActiveIs(
            @NotNull final Long equipmentId,
            final boolean active
    );
}