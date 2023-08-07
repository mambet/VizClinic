package ru.viz.clinic.component.dialog;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.shared.Registration;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import ru.viz.clinic.data.entity.Engineer;
import ru.viz.clinic.data.entity.Order;

import java.util.Objects;

public abstract class RecordDialog extends ConfirmDialog {
    protected final TextArea commentTextArea = new TextArea("Comment");
    private final Button btnConfirm = new Button("Confirm");

    public RecordDialog() {
        setConfirmButton(btnConfirm);
        commentTextArea.setRequired(true);
        commentTextArea.setClearButtonVisible(true);
        commentTextArea.addValueChangeListener(this::valuesChanges);
        commentTextArea.setSizeFull();
        setHeader("Комментарий");
        this.add(commentTextArea);
    }

    private void valuesChanges(AbstractField.ComponentValueChangeEvent<TextArea, String> textAreaStringComponentValueChangeEvent) {
        btnConfirm.setEnabled(!commentTextArea.getValue().isEmpty());
    }
}
