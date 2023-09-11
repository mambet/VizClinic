package ru.viz.clinic.component.dialog;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import jakarta.validation.constraints.NotNull;
import ru.viz.clinic.component.grid.RecordGrid;
import ru.viz.clinic.data.entity.Record;

import java.util.Set;

import static ru.viz.clinic.help.Translator.BTN_CANCEL;

public class ShowRecordDialog extends ConfirmDialog {
    private static final int WIDTH = 1000;

    public ShowRecordDialog(@NotNull final Set<Record> records) {
        this.setConfirmText(BTN_CANCEL);
        this.setWidth(WIDTH, Unit.PIXELS);
        this.add(RecordGrid.createRecordGrid(records));
    }
}