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
import ru.viz.clinic.component.dialog.RecordCreateCommentDialog;
import ru.viz.clinic.component.dialog.RecordCreateLeaveDialog;
import ru.viz.clinic.data.OrderState;
import ru.viz.clinic.data.entity.Engineer;
import ru.viz.clinic.data.entity.Order;
import ru.viz.clinic.service.RecordService;

import java.util.Objects;

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
        })).setHeader("Manage").setAutoWidth(true);
    }

    public void commentOrder(
            Order order,
            String comment
    ) {
        fireEvent(new CommentEvent(this, order, comment));
    }

    public void leaveOrder(
            Order order,
            String comment
    ) {
        fireEvent(new LeaveEvent(this, order, comment));
    }

    //for Engineer
    private Button adoptOrderButton(@NotNull final Order order) {
        Objects.requireNonNull(order);
        Button button = new Button(new Icon(VaadinIcon.SCREWDRIVER));
        button.setTooltipText("Принять");
        button.addClickListener(e -> {
            fireEvent(new AdoptEvent(this, order));
        });
        return button;
    }

    //for Engineer
    private Button leaveOrderButton(@NotNull final Order order) {
        Button button = new Button(new Icon(VaadinIcon.EXIT_O), e -> {
            new RecordCreateLeaveDialog(Objects.requireNonNull(order), this::leaveOrder).open();
        });
        button.setTooltipText("Вернуть");
        return button;
    }

    //for Engineer
    private Button commentOrderButton(@NotNull final Order order) {
        Button button = new Button(new Icon(VaadinIcon.COMMENT_O),
                e -> new RecordCreateCommentDialog(Objects.requireNonNull(order), this::commentOrder).open());
        button.setTooltipText("Комментировать");
        return button;
    }

    @Getter
    public abstract static class OrderAbstractEvent<T extends Component> extends ComponentEvent<T> {
        private final Order order;

        protected OrderAbstractEvent(
                @NotNull final T source,
                @NotNull final Order order
        ) {
            super(Objects.requireNonNull(source), false);
            this.order = Objects.requireNonNull(order);
        }
    }

    public static class AdoptEvent extends OrderAbstractEvent<EngineerOrderGrid> {
        public AdoptEvent(
                @NotNull final EngineerOrderGrid source,
                @NotNull final Order order
        ) {
            super(Objects.requireNonNull(source), Objects.requireNonNull(order));
        }
    }

    @Getter
    public static class CommentEvent extends OrderAbstractEvent<EngineerOrderGrid> {
        private final String comment;

        public CommentEvent(
                @NotNull final EngineerOrderGrid source,
                @NotNull final Order order,
                @NotNull final String comment
        ) {
            super(Objects.requireNonNull(source), Objects.requireNonNull(order));
            this.comment = comment;
        }
    }

    @Getter
    public static class LeaveEvent extends OrderAbstractEvent<EngineerOrderGrid> {
        private final String comment;

        public LeaveEvent(
                @NotNull final EngineerOrderGrid source,
                @NotNull final Order order,
                @NotNull final String comment

        ) {
            super(Objects.requireNonNull(source), Objects.requireNonNull(order));
            this.comment = comment;
        }
    }
}
