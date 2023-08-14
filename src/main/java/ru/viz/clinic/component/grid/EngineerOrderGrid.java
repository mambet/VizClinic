package ru.viz.clinic.component.grid;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import ru.viz.clinic.data.OrderState;
import ru.viz.clinic.data.entity.Engineer;
import ru.viz.clinic.data.entity.Order;
import ru.viz.clinic.service.RecordService;

import java.util.Objects;

import static ru.viz.clinic.help.Translator.*;

public class EngineerOrderGrid extends OrderGrid {
    private final Engineer engineer;

    public EngineerOrderGrid(
            @NotNull final Engineer engineer,
            @NotNull final RecordService recordService
    ) {
        super(Objects.requireNonNull(recordService));
        this.engineer = Objects.requireNonNull(engineer);
        addButton();
    }

    public void addButton() {
        this.addColumn(new ComponentRenderer<>(HorizontalLayout::new, (layout, order) -> {
            if (order.getDestinationEngineers().contains(engineer)
                    && order.getOrderState().equals(OrderState.READY)) {
                layout.add(adoptOrderButton(order));
            }
            if (order.getOwnerEngineer() != null
                    && order.getOwnerEngineer().equals(engineer)
                    && order.getOrderState().equals(OrderState.WORKING)) {
                layout.add(commentOrderButton(order), leaveOrderButton(order));
            }
            layout.add(showRecords(order));
        })).setAutoWidth(true);
    }

    //for Engineer
    private Button adoptOrderButton(@NotNull final Order order) {
        Objects.requireNonNull(order);
        final Button button = new Button(new Icon(VaadinIcon.SCREWDRIVER), e -> fireEvent(new AdoptGridEvent(this,
                Objects.requireNonNull(order))));
        button.setTooltipText(TTP_ADOPT_ORDER);
        return button;
    }

    //for Engineer
    private Button leaveOrderButton(@NotNull final Order order) {
        final Button button = new Button(new Icon(VaadinIcon.EXIT_O), e -> fireEvent(new LeaveGridEvent(this,
                Objects.requireNonNull(order))));
        button.setTooltipText(TTP_LEAVE_ORDER);
        return button;
    }

    //for Engineer
    private Button commentOrderButton(@NotNull final Order order) {
        final Button button = new Button(new Icon(VaadinIcon.COMMENT_O), e -> fireEvent(new CommentGridEvent(this,
                Objects.requireNonNull(order))));
        button.setTooltipText(TTP_COMMENT_ORDER);
        return button;
    }

    public static class AdoptGridEvent extends AbstractGridEvent<EngineerOrderGrid, Order> {
        public AdoptGridEvent(
                @NotNull final EngineerOrderGrid source,
                @NotNull final Order order
        ) {
            super(Objects.requireNonNull(source), Objects.requireNonNull(order));
        }
    }

    @Getter
    public static class CommentGridEvent extends AbstractGridEvent<EngineerOrderGrid, Order> {
        public CommentGridEvent(
                @NotNull final EngineerOrderGrid source,
                @NotNull final Order order
        ) {
            super(Objects.requireNonNull(source), Objects.requireNonNull(order));
        }
    }

    @Getter
    public static class LeaveGridEvent extends AbstractGridEvent<EngineerOrderGrid, Order> {
        public LeaveGridEvent(
                @NotNull final EngineerOrderGrid source,
                @NotNull final Order order

        ) {
            super(Objects.requireNonNull(source), Objects.requireNonNull(order));
        }
    }
}
