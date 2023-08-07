package ru.viz.clinic.data.repository;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.viz.clinic.data.entity.Department;
import ru.viz.clinic.data.entity.Engineer;
import ru.viz.clinic.data.entity.Hospital;
import ru.viz.clinic.data.entity.Order;
import ru.viz.clinic.data.entity.Record;

import java.util.List;
import java.util.Set;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.department.hospital.id = :hospitalId")
    List<Order> getByHospital(@Param("hospitalId") Long hospitalId);
    @Query("SELECT o FROM Order o WHERE o.department.id = :departmentId")
    List<Order> getByDepartment(@Param("departmentId") Long departmentId);

}
