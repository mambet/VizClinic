package ru.viz.clinic.component.dialog;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.textfield.TextArea;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

import static ru.viz.clinic.help.Translator.*;

public abstract class RecordDialog extends ConfirmDialog {
    protected final TextArea commentTextArea = new TextArea(LBL_COMMENT);
    private final Button btnConfirm;

    public RecordDialog(
            @NotNull final String header,
            @NotNull final String confirmText
    ) {
        Objects.requireNonNull(header);
        Objects.requireNonNull(confirmText);

        setHeader(header);

        btnConfirm = new Button(confirmText);
        btnConfirm.setEnabled(false);

        this.setConfirmButton(btnConfirm);
        this.setCancelable(true);
        this.setCancelText(BTN_CANCEL);

        commentTextArea.setRequired(true);
        commentTextArea.setClearButtonVisible(true);
        commentTextArea.addValueChangeListener(this::valuesChanges);
        commentTextArea.setSizeFull();

        this.add(commentTextArea);
    }

    private void valuesChanges(AbstractField.ComponentValueChangeEvent<TextArea, String> textAreaStringComponentValueChangeEvent) {
        btnConfirm.setEnabled(!commentTextArea.getValue().isEmpty());
    }
}