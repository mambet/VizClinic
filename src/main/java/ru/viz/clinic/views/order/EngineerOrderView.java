package ru.viz.clinic.views.order;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.constraints.NotNull;
import lombok.extern.log4j.Log4j2;
import ru.viz.clinic.component.TopicBox;
import ru.viz.clinic.component.grid.EngineerOrderGrid;
import ru.viz.clinic.data.EventType;
import ru.viz.clinic.data.OrderState;
import ru.viz.clinic.data.entity.Engineer;
import ru.viz.clinic.data.entity.Order;
import ru.viz.clinic.help.Helper;
import ru.viz.clinic.security.AuthenticationService;
import ru.viz.clinic.service.OrderService;
import ru.viz.clinic.service.PersonalService;
import ru.viz.clinic.service.RecordService;
import ru.viz.clinic.views.MainLayout;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static ru.viz.clinic.help.Translator.*;

@PageTitle("Заявки")
@Route(value = "EngineerOrderView", layout = MainLayout.class)
@RolesAllowed("ENGINEER")
@Log4j2
public class EngineerOrderView extends OrderView<EngineerOrderGrid> {
    private final Engineer engineer;

    public EngineerOrderView(
            @NotNull final OrderService orderService,
            @NotNull final RecordService recordService,
            @NotNull final PersonalService personalService,
            @NotNull final AuthenticationService authenticationService
    ) {
        super(Objects.requireNonNull(orderService),
                Objects.requireNonNull(recordService),
                Objects.requireNonNull(personalService),
                Objects.requireNonNull(authenticationService));

        this.engineer = Objects.requireNonNull(getEngineer());
        this.orderGrid = new EngineerOrderGrid(engineer, Objects.requireNonNull(recordService));
        this.orderGrid.setItems(orderService.getByHospital(engineer.getHospital().getId()));
        this.orderGrid.addListener(EngineerOrderGrid.AdoptEvent.class, this::handleAdoptEvent);
        this.orderGrid.addListener(EngineerOrderGrid.CommentEvent.class, this::handleCommentEvent);
        this.orderGrid.addListener(EngineerOrderGrid.LeaveEvent.class, this::handleLeaveEvent);

        this.add(orderGrid);
    }

    private Engineer getEngineer() {
        AtomicReference<Engineer> atomicReference = new AtomicReference<>();
        authenticationService.getUserDetails().flatMap(personalService::getLoggedPersonal)
                .ifPresent(personal -> {
                    if (personal instanceof Engineer e) {
                        atomicReference.set(e);
                    }
                });
        return atomicReference.get();
    }

    private void handleAdoptEvent(@NotNull final EngineerOrderGrid.AdoptEvent event) {
        Order order = Objects.requireNonNull(event.getOrder());
        order.setOwnerEngineer(engineer);
        order.setOrderState(OrderState.WORKING);
        saveOrder(order).ifPresent(savedOrder -> {
            recordService.createRecord(EventType.ADOPT_ORDER, engineer,  savedOrder); //TODO return optional and handle
            updateGrid();
            Helper.showSuccessNotification(MSG_ORDER_SUCCESS_ADOPT);
            log.info("adopt order id {}", order.getId());
        });

    }

    private void handleCommentEvent(@NotNull final EngineerOrderGrid.CommentEvent event) {
        Objects.requireNonNull(event);
        final Order order = Objects.requireNonNull(event.getOrder());
        final String comment = Objects.requireNonNull(event.getComment());
        recordService.createRecord(EventType.NOTE, engineer, order, comment);
        updateGrid();
        log.info("comment order id {}", order.getId());
    }

    private void handleLeaveEvent(EngineerOrderGrid.LeaveEvent event) {
        Order order = Objects.requireNonNull(event.getOrder());
        order.setOwnerEngineer(null);
        event.getOrder().setOrderState(OrderState.READY);
        saveOrder(order).ifPresent(savedOrder -> {
            recordService.createRecord(EventType.LIVE_ORDER, engineer, savedOrder, event.getComment());
            updateGrid();
            Helper.showSuccessNotification(MSG_ORDER_SUCCESS_LEAVED);
            log.info("leave order id {}", order.getId());
        });
    }

    private void updateGrid() {
        orderGrid.setItems(orderService.getByHospital(engineer.getHospital().getId()));
        orderGrid.getListDataView().refreshAll();
    }
}
