package ru.viz.clinic.component.grid;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import ru.viz.clinic.data.entity.Record;

import java.util.Collection;

public class RecordGrid extends Grid<Record> {
    public RecordGrid() {
        createTable();
    }

    private void createTable() {
        this.setSelectionMode(SelectionMode.NONE);
        this.addColumn(new LocalDateTimeRenderer<>(Record::getEventTime, "dd.MM.yyyy HH:mm"))
                .setSortable(true)
                .setComparator(Record::getEventTime)
                .setAutoWidth(true);
        this.addColumn(Record::getPerson).setAutoWidth(true);
        this.addColumn(record -> record.getEventType().getValue()).setAutoWidth(true);
        this.addColumn(Record::getComment).setAutoWidth(true);
        this.setAllRowsVisible(true);
        this.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
    }

    @Override
    public GridListDataView<Record> setItems(Collection<Record> items) {
        GridListDataView<Record> recordGridListDataView = super.setItems(items);
        this.getListDataView().setSortOrder(Record::getEventTime, SortDirection.ASCENDING);
        return recordGridListDataView;
    }
}
