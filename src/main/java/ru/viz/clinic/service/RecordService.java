package ru.viz.clinic.service;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
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
    public Optional<Record> save(Record record) {
        try {
            Optional<Record> optionalRecord = Optional.of(recordRepository.save(record));
            Helper.showSuccessNotification(MSG_RECORD_UPDATED);
            return optionalRecord;
        } catch (Exception e) {
            Helper.showErrorNotification(ERR_RECORD_SAVED_FAILED);
            log.error("saving record failed, with error", e);
        }
        return Optional.empty();
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

    public Optional<Record> addRecord(
            @NotNull final EventType eventType,
            @NotNull final Personal personal,
            @NotNull final Order order,
            @NotNull final String comment
    ) {
        Record record = new Record();
        record.setEventType(Objects.requireNonNull(eventType));
        record.setPerson(PersonalToStringConverter.convertToPresentation(Objects.requireNonNull(personal)));
        record.setComment(Objects.requireNonNull(comment));
        record.setOrder(Objects.requireNonNull(order));
        return save(record);
    }

    public Optional<Record> addRecord(
            @NotNull final EventType eventType,
            @NotNull final Personal personal,
            @NotNull final Order order
    ) {
        return addRecord(Objects.requireNonNull(eventType),
                Objects.requireNonNull(personal),
                Objects.requireNonNull(order),
                Strings.EMPTY);
    }

    @Transactional
    public Set<Record> getByOrderId(@NotNull final Long orderId) {
        return recordRepository.getRecordsByOrderId(orderId);
    }
}