package ru.viz.clinic.service;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.viz.clinic.data.EventType;
import ru.viz.clinic.data.entity.*;
import ru.viz.clinic.data.entity.Record;
import ru.viz.clinic.help.Helper;
import ru.viz.clinic.repository.RecordRepository;

import java.util.Objects;
import java.util.Set;

import static ru.viz.clinic.help.Helper.formatAndShowErrorMessage;
import static ru.viz.clinic.help.Helper.showErrorNotification;

@Service
@Slf4j
public class RecordService extends AbstractService<Record, RecordRepository> {
    @Autowired
    public RecordService(@NotNull final RecordRepository recordRepository) {
        super(recordRepository);
    }

    public void addRecord(
            @NotNull final EventType eventType,
            @NotNull final Personal personal,
            @NotNull final Order order,
            @NotNull final String comment
    ) {
        final Record record = new Record();
        record.setEventType(Objects.requireNonNull(eventType));
        record.setComment(Objects.requireNonNull(comment));
        record.setOrder(Objects.requireNonNull(order));
        if (personal instanceof final Engineer engineer) {
            record.setEngineer(engineer);
        }
        if (personal instanceof final Medic medic) {
            record.setMedic(medic);
        }
        if (personal instanceof final Admin admin) {
            record.setAdmin(admin);
        }
        save(record, r -> {
        }, r -> formatAndShowErrorMessage("Протокол '%s' не обнавлен", r));
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

    public Set<Record> getByOrderId(@NotNull final Long orderId) {
        return repository.getRecordsByOrderId(Objects.requireNonNull(orderId));
    }

    public Set<Record> getByMedicId(@NotNull final Long medicId) {
        return repository.getByMedicId(Objects.requireNonNull(medicId));
    }
}