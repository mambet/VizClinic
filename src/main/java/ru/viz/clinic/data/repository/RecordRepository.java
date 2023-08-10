package ru.viz.clinic.data.repository;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.viz.clinic.data.entity.Record;

import java.util.Set;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long>, JpaSpecificationExecutor<Record> {
    Set<Record> getRecordsByOrderId(@NotNull final Long id);
}
