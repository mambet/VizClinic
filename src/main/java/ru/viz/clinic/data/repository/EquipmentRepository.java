package ru.viz.clinic.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.viz.clinic.data.entity.Equipment;
import ru.viz.clinic.data.entity.MedicPersonal;

import java.util.List;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
}
