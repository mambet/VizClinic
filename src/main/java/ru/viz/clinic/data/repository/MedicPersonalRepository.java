package ru.viz.clinic.data.repository;

import org.springframework.stereotype.Repository;
import ru.viz.clinic.data.entity.Medic;

import java.util.List;

@Repository
public interface MedicPersonalRepository extends CommonRepository<Medic> {
    @Override
    List<Medic> findByUsername(String username);
}
