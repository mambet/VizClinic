package ru.viz.clinic.component.grid;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.data.provider.SortDirection;
import jakarta.validation.constraints.NotNull;
import ru.viz.clinic.converter.EntityToStringConverter;
import ru.viz.clinic.data.entity.Record;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

import static ru.viz.clinic.converter.EntityToStringConverter.convertToPresentation;
import static ru.viz.clinic.help.Helper.getDateTimeRenderer;

public class RecordGrid extends Grid<Record> {
    private RecordGrid(@NotNull final Set<Record> records) {
        Objects.requireNonNull(records);

        this.addColumn(getDateTimeRenderer(Record::getRecordTime))
                .setComparator(Record::getRecordTime)
                .setAutoWidth(true);
        this.addColumn(record -> convertToPresentation(record.getMedic())).setAutoWidth(true);
        this.addColumn(record -> convertToPresentation(record.getEngineer())).setAutoWidth(true);
        this.addColumn(record -> convertToPresentation(record.getAdmin())).setAutoWidth(true);
        this.addColumn(record -> record.getEventType().getValue()).setAutoWidth(true);
        this.addColumn(Record::getComment).setAutoWidth(true);
        this.setSelectionMode(SelectionMode.NONE);
        this.setAllRowsVisible(true);
        this.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);

        setItems(records);
    }

    public static RecordGrid createRecordGrid(@NotNull final Set<Record> records) {
        return new RecordGrid(records);
    }

    @Override
    public GridListDataView<Record> setItems(@NotNull final Collection<Record> items) {
        Objects.requireNonNull(items);
        final GridListDataView<Record> recordGridListDataView = super.setItems(items);
        this.getListDataView().setSortOrder(record -> {
            if (record.getRecordTime() == null) {
                return LocalDateTime.now();
            } else {
                return record.getRecordTime();
            }
        }, SortDirection.ASCENDING);

        return recordGridListDataView;
    }
}