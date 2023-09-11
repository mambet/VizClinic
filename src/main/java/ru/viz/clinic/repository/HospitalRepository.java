package ru.viz.clinic.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.viz.clinic.data.entity.Hospital;

@Repository
public interface HospitalRepository extends CommonRepository<Hospital>, JpaSpecificationExecutor<Hospital> {

}
