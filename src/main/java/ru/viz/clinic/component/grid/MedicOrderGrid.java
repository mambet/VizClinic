package ru.viz.clinic.component.grid;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import jakarta.validation.constraints.NotNull;
import ru.viz.clinic.component.dialog.OrderCloseDialog;
import ru.viz.clinic.data.OrderState;
import ru.viz.clinic.data.entity.Order;
import ru.viz.clinic.service.RecordService;

import java.util.Objects;

import static ru.viz.clinic.help.Translator.*;

public class MedicOrderGrid extends OrderGrid {
    public MedicOrderGrid(@NotNull final RecordService recordService) {
        super(Objects.requireNonNull(recordService));
        addButtons();
    }

    private void addButtons() {
        this.addColumn(new ComponentRenderer<>(HorizontalLayout::new, (layout, order) -> {
            if (order.getOrderState().equals(OrderState.READY)) {
                layout.add(editOrderButton(order));
            }
            if (order.getOrderState().equals(OrderState.WORKING)) {
                layout.add(closeOrderButton(order));
            }
            layout.add(showRecords(order));
        })).setAutoWidth(true);
    }

    private Button editOrderButton(@NotNull final Order order) {
        Objects.requireNonNull(order);
        final Button button = new Button(new Icon(VaadinIcon.EDIT));
        button.setTooltipText(TTP_MODIFY_ORDER);
        button.addClickListener(e -> fireEvent(new UpdateGridEvent(this, order)));
        return button;
    }

    private Button closeOrderButton(@NotNull final Order order) {
        Objects.requireNonNull(order);
        final Button button = new Button(new Icon(VaadinIcon.CLOSE_CIRCLE_O));
        button.setTooltipText(TTP_CLOSE_ORDER);
        button.addClickListener(e -> {
            final OrderCloseDialog orderCloseDialog = new OrderCloseDialog();
            orderCloseDialog.addConfirmListener(confirmEvent -> fireEvent(new CloseGridEvent(this, order)));
            orderCloseDialog.open();
        });
        return button;
    }

    public static class UpdateGridEvent extends AbstractGridEvent<MedicOrderGrid, Order> {
        public UpdateGridEvent(
                @NotNull final MedicOrderGrid source,
                @NotNull final Order order
        ) {
            super(Objects.requireNonNull(source), Objects.requireNonNull(order));
        }
    }

    public static class CloseGridEvent extends AbstractGridEvent<MedicOrderGrid, Order> {
        public CloseGridEvent(
                @NotNull final MedicOrderGrid source,
                @NotNull final Order order
        ) {
            super(Objects.requireNonNull(source), Objects.requireNonNull(order));
        }
    }

    public static class DeleteGridEvent extends AbstractGridEvent<MedicOrderGrid, Order> {
        public DeleteGridEvent(
                @NotNull final MedicOrderGrid source,
                @NotNull final Order order
        ) {
            super(Objects.requireNonNull(source), Objects.requireNonNull(order));
        }
    }
}
