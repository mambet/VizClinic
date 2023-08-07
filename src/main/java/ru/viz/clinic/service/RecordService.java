package ru.viz.clinic.service;

import jakarta.validation.constraints.NotNull;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.viz.clinic.converter.PersonalToStringConverter;
import ru.viz.clinic.data.EventType;
import ru.viz.clinic.data.entity.Order;
import ru.viz.clinic.data.entity.Personal;
import ru.viz.clinic.data.repository.RecordRepository;
import ru.viz.clinic.data.entity.Record;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class RecordService {
    private final RecordRepository recordRepository;

    @Autowired
    public RecordService(@NotNull final RecordRepository recordRepository) {
        this.recordRepository = Objects.requireNonNull(recordRepository);
    }

    // Save or update a record
    public Record save(Record record) {
        return recordRepository.save(record);
    }

    // Get all records
    public List<Record> getAll() {
        return recordRepository.findAll();
    }

    // Get record by ID
    public Record getRecordById(Long id) {
        return recordRepository.findById(id).orElse(null);
    }

    // Delete a record by ID
    public void deleteRecordById(Long id) {
        recordRepository.deleteById(id);
    }

    public void createRecord(
            EventType eventType,
            Personal personal,
            Order order,
            String comment
    ) {
        Record record = new Record();
        record.setEventType(eventType);
        record.setPerson(PersonalToStringConverter.convertToPresentation(personal));
        record.setComment(comment);
        record.setOrder(order);
        save(record);
    }

    public void createRecord(
            EventType eventType,
            Personal personal,
            Order order
    ) {
        createRecord(eventType, personal, order, Strings.EMPTY);
    }

    @Transactional
    public Set<Record> getByOrderId(@NotNull final Long orderId) {
        return recordRepository.getRecordsByOrderId(orderId);
    }
}