package ru.viz.clinic.component.dialog;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public abstract class VizConfirmDialog extends ConfirmDialog {
    private final Button btnConfirm = new Button();
    private Binder<?> binder;

    public VizConfirmDialog(
            @NotNull final String header

    ) {
        setHeader(Objects.requireNonNull(header));
        setConfirmButton(btnConfirm);
        setCancelable(true);
        addConfirmListener(this::confirmListener);
    }

    protected void setBinder(@NotNull final Binder<?> binder) {
        this.binder = binder;
        this.binder.addValueChangeListener(this::valueChanges);
        setBtnConfirmEnable(binder.isValid());
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

    abstract void confirmListener(ConfirmEvent confirmEvent);

    @Override
    public <T extends ComponentEvent<?>> Registration addListener(
            Class<T> eventType,
            ComponentEventListener<T> listener
    ) {
        return getEventBus().addListener(eventType, listener);
    }
}
