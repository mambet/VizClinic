package ru.viz.clinic.component.dialog;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import ru.viz.clinic.help.Helper;

import java.util.Objects;

import static ru.viz.clinic.help.Translator.*;

public abstract class VizConfirmDialog<T> extends ConfirmDialog {
    private final Button btnConfirm = new Button();

    protected final Binder<T> binder = new Binder<>();
    protected final T item;

    public VizConfirmDialog(
            @NotNull final String header,
            @NotNull final T item

    ) {
        this.item = Objects.requireNonNull(item);
        setHeader(Objects.requireNonNull(header));
        setCancelable(true);
        setConfirmButton(btnConfirm);
        setCancelText(BTN_CANCEL);
        this.binder.addValueChangeListener(this::valueChanges);
        setBtnConfirmEnable(false);
    }

    protected void initCreate() {
        addConfirmListener(this::createListener);
        setConfirmText(BTN_CONFIRM_CREATE);
    }

    protected void initUpdate() {
        addConfirmListener(this::updateListener);
        setConfirmText(BTN_CONFIRM_UPDATE);
    }

    private void valueChanges(final HasValue.ValueChangeEvent<?> valueChangeEvent) {
        setBtnConfirmEnable(binder.isValid());
    }

    @Override
    public void setConfirmText(final String confirmText) {
        btnConfirm.setText(confirmText);
    }

    protected void setBtnConfirmEnable(final boolean enable) {
        btnConfirm.setEnabled(enable);
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

    void createListener(final ConfirmEvent confirmEvent) {
        if (writeBinder()) {
            handleCreate();
        }
    }

    void updateListener(final ConfirmEvent confirmEvent) {
        if (writeBinder()) {
            handleUpdate();
        }
    }

    private boolean writeBinder() {
        if (binder.isValid()) {
            binder.writeBeanIfValid(Objects.requireNonNull(item));
            this.close();
            return true;
        } else {
            Helper.showErrorNotification(ERR_MSG_INVALID_DATA);
            return false;
        }
    }

    protected abstract void handleCreate();

    protected abstract void handleUpdate();
}