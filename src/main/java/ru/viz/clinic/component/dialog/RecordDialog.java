package ru.viz.clinic.component.dialog;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.shared.Registration;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

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

    private void valuesChanges(final AbstractField.ComponentValueChangeEvent<TextArea, String> textAreaStringComponentValueChangeEvent) {
        btnConfirm.setEnabled(!commentTextArea.getValue().isEmpty());
    }
    @Override
    public <E extends ComponentEvent<?>> Registration addListener(
            final Class<E> eventType,
            final ComponentEventListener<E> listener
    ) {
        return getEventBus().addListener(eventType, listener);
    }

    @Getter
    public abstract static class AbstractDialogEvent<T extends Component, E> extends ComponentEvent<T> {
        private final E entity;

        protected AbstractDialogEvent(
                @NotNull final T source,
                @NotNull final E entity
        ) {
            super(Objects.requireNonNull(source), false);
            this.entity = Objects.requireNonNull(entity);
        }
    }
}