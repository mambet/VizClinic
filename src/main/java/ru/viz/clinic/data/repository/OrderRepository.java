package ru.viz.clinic.data.repository;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.viz.clinic.data.entity.Order;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.equipment.department.hospital.id = :hospitalId")
    List<Order> getByHospital(@Param("hospitalId") Long hospitalId);

    @Query("SELECT o FROM Order o WHERE o.equipment.department.id = :departmentId")
    List<Order> getByDepartmentId(@Param("departmentId") final Long id);
}