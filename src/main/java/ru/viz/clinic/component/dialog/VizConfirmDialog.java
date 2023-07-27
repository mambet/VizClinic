package ru.viz.clinic.component.dialog;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import jakarta.validation.constraints.NotNull;
import ru.viz.clinic.help.Helper;

import java.util.Objects;

import static ru.viz.clinic.help.Translator.*;

public abstract class VizConfirmDialog<T> extends ConfirmDialog {
    private final Button btnConfirm = new Button();
    protected final Binder<T> binder = new Binder<>();
    ;
    protected final T item;

    public VizConfirmDialog(
            @NotNull final String header,
            @NotNull final T item

    ) {
        this.item = Objects.requireNonNull(item);
        this.binder.readBean(Objects.requireNonNull(item));
        setHeader(Objects.requireNonNull(header));
        setConfirmButton(btnConfirm);
        setCancelable(true);
        addConfirmListener(this::confirmListener);
        setConfirmText(BTN_CONFIRM_CREATE);
        setCancelText(BTN_CANCEL);
        this.binder.addValueChangeListener(this::valueChanges);
        setBtnConfirmEnable(false);
    }

    private void valueChanges(HasValue.ValueChangeEvent<?> valueChangeEvent) {
        setBtnConfirmEnable(binder.isValid());
    }

    @Override
    public void setConfirmText(String confirmText) {
        btnConfirm.setText(confirmText);
    }

    protected void setBtnConfirmEnable(final boolean enable) {
        btnConfirm.setEnabled(enable);
    }

    @Override
    public <E extends ComponentEvent<?>> Registration addListener(
            Class<E> eventType,
            ComponentEventListener<E> listener
    ) {
        return getEventBus().addListener(eventType, listener);
    }

    void confirmListener(ConfirmEvent confirmEvent) {
        if (binder.isValid()) {
            binder.writeBeanIfValid(Objects.requireNonNull(item));
            firePersonalEvent();
            this.close();
        } else {
            Helper.showErrorNotification(ERR_MSG_INVALID_DATA);
        }
    }

    protected abstract void firePersonalEvent();
}