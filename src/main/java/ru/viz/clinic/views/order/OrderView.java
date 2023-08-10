package ru.viz.clinic.views.order;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jakarta.validation.constraints.NotNull;
import ru.viz.clinic.component.grid.OrderGrid;
import ru.viz.clinic.service.OrderService;
import ru.viz.clinic.service.RecordService;

import java.util.Objects;

public abstract class OrderView<T extends OrderGrid> extends VerticalLayout {
    protected final OrderService orderService;
    protected final RecordService recordService;
    protected T orderGrid;

    public OrderView(
            @NotNull final OrderService orderService,
            @NotNull final RecordService recordService
    ) {
        this.orderService = Objects.requireNonNull(orderService);
        this.recordService = Objects.requireNonNull(recordService);
    }
}
