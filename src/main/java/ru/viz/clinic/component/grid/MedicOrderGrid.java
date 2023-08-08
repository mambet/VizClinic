package ru.viz.clinic.component.grid;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import ru.viz.clinic.converter.PersonalToStringConverter;
import ru.viz.clinic.data.OrderState;
import ru.viz.clinic.data.entity.Order;
import ru.viz.clinic.service.OrderService;
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
        Button button = new Button(new Icon(VaadinIcon.EDIT));
        button.setTooltipText(TTP_MODIFY_ORDER);
        button.addClickListener(e -> fireEvent(new EditEvent(this, order)));
        return button;
    }

    private Button closeOrderButton(@NotNull final Order order) {
        Objects.requireNonNull(order);
        Button button = new Button(new Icon(VaadinIcon.CLOSE_CIRCLE_O));
        button.setTooltipText(TTP_CLOSE_ORDER);
        button.addClickListener(e -> fireEvent(new CloseEvent(this, order)));
        return button;
    }



    public static class EditEvent extends OrderAbstractEvent<MedicOrderGrid> {
        public EditEvent(
                @NotNull final MedicOrderGrid source,
                @NotNull final Order order
        ) {
            super(Objects.requireNonNull(source), Objects.requireNonNull(order));
        }
    }

    public static class CloseEvent extends OrderAbstractEvent<MedicOrderGrid> {
        public CloseEvent(
                @NotNull final MedicOrderGrid source,
                @NotNull final Order order
        ) {
            super(Objects.requireNonNull(source), Objects.requireNonNull(order));
        }
    }
}
