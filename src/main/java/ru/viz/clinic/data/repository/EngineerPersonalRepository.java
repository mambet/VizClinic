package ru.viz.clinic.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.viz.clinic.data.entity.EngineerPersonal;
import ru.viz.clinic.data.entity.MedicPersonal;
import ru.viz.clinic.data.entity.Personal;

import java.util.List;

@Repository
public interface EngineerPersonalRepository extends CommonRepository<EngineerPersonal> {
    @Override
    List<EngineerPersonal> findByUsername(String username);
}
