package ru.viz.clinic.views.order;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jakarta.validation.constraints.NotNull;
import org.apache.logging.log4j.util.Strings;
import ru.viz.clinic.component.grid.OrderGrid;
import ru.viz.clinic.data.EventType;
import ru.viz.clinic.data.entity.Order;
import ru.viz.clinic.data.entity.Personal;
import ru.viz.clinic.data.entity.Record;
import ru.viz.clinic.help.Helper;
import ru.viz.clinic.help.Translator;
import ru.viz.clinic.security.AuthenticationService;
import ru.viz.clinic.service.OrderService;
import ru.viz.clinic.service.PersonalService;
import ru.viz.clinic.service.RecordService;

import java.util.Objects;

public abstract class OrderView<T extends OrderGrid> extends VerticalLayout {
    protected final OrderService orderService;
    protected final RecordService recordService;
    protected final PersonalService personalService;
    protected final AuthenticationService authenticationService;
    protected T orderGrid;

    public OrderView(
            @NotNull final OrderService orderService,
            @NotNull final RecordService recordService,
            @NotNull final PersonalService personalService,
            @NotNull final AuthenticationService authenticationService
    ) {
        this.orderService = Objects.requireNonNull(orderService);
        this.recordService = Objects.requireNonNull(recordService);
        this.personalService = Objects.requireNonNull(personalService);
        this.authenticationService = Objects.requireNonNull(authenticationService);
    }

    protected Order saveOrder(@NotNull final Order order) {
        Order savedOrder = null;
        try {
            savedOrder = orderService.save(order);
            Helper.showSuccessNotification(Translator.MSG_HOSPITAL_SUCCESS_SAVED);
        } catch (Exception e) {
            Helper.showErrorNotification(String.format("жопа %s", e.getMessage()));
        }
        return savedOrder;
    }
}
