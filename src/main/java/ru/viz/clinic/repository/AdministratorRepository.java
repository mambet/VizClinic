package ru.viz.clinic.repository;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.viz.clinic.data.entity.Admin;
import ru.viz.clinic.data.entity.Medic;

import java.util.Set;

@Repository
public interface AdministratorRepository extends CommonPersonalRepository<Admin>, JpaSpecificationExecutor<Admin> {
}
