package ru.viz.clinic.component.dialog;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import jakarta.validation.constraints.NotNull;
import ru.viz.clinic.component.grid.RecordGrid;
import ru.viz.clinic.data.entity.Record;

import java.util.Set;

public class ShowRecordDialog extends ConfirmDialog {
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 700;

    public ShowRecordDialog(@NotNull final Set<Record> records) {
        RecordGrid recordGrid = new RecordGrid();
        recordGrid.setItems(records);

        this.setWidth(WIDTH, Unit.PIXELS);
        this.setHeight(HEIGHT, Unit.PIXELS);
        this.add(recordGrid);
    }
}
