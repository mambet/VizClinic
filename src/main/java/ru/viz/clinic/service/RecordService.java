package ru.viz.clinic.service;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.viz.clinic.data.EventType;
import ru.viz.clinic.data.entity.*;
import ru.viz.clinic.data.entity.Record;
import ru.viz.clinic.data.repository.RecordRepository;
import ru.viz.clinic.help.Helper;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static ru.viz.clinic.help.Translator.*;

@Service
@Slf4j
public class RecordService {
    private final RecordRepository recordRepository;

    @Autowired
    public RecordService(@NotNull final RecordRepository recordRepository) {
        this.recordRepository = Objects.requireNonNull(recordRepository);
    }

    @Transactional
    public void save(Record record) {
        try {
            recordRepository.save(record);
            Helper.showSuccessNotification(MSG_RECORD_UPDATED);
        } catch (Exception e) {
            Helper.showErrorNotification(ERR_RECORD_SAVED_FAILED);
            log.error("saving record failed, with error", e);
        }
    }

    public List<Record> getAll() {
        return recordRepository.findAll();
    }

    public Record getRecordById(Long id) {
        return recordRepository.findById(id).orElse(null);
    }

    public void deleteRecordById(Long id) {
        recordRepository.deleteById(id);
    }

    public void addRecord(
            @NotNull final EventType eventType,
            @NotNull final Personal personal,
            @NotNull final Order order,
            @NotNull final String comment
    ) {
        Record record = new Record();
        record.setEventType(Objects.requireNonNull(eventType));
        record.setComment(Objects.requireNonNull(comment));
        record.setOrder(Objects.requireNonNull(order));
        if (personal instanceof final Engineer engineer) {
            record.setEngineer(engineer);
        }
        if (personal instanceof final Medic medic) {
            record.setMedic(medic);
        }
        save(record);
    }

    public void addRecord(
            @NotNull final EventType eventType,
            @NotNull final Personal personal,
            @NotNull final Order order
    ) {
        addRecord(Objects.requireNonNull(eventType),
                Objects.requireNonNull(personal),
                Objects.requireNonNull(order),
                Strings.EMPTY);
    }

    @Transactional
    public Set<Record> getByOrderId(@NotNull final Long orderId) {
        return recordRepository.getRecordsByOrderId(orderId);
    }
}