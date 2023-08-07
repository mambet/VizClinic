package ru.viz.clinic.data.repository;

import org.springframework.stereotype.Repository;
import ru.viz.clinic.data.entity.Engineer;

import java.util.List;

@Repository
public interface EngineerPersonalRepository extends CommonRepository<Engineer> {
    @Override
    List<Engineer> findByUsername(String username);
}
