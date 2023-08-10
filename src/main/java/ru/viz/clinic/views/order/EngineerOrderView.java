package ru.viz.clinic.views.order;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.constraints.NotNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import ru.viz.clinic.component.grid.EngineerOrderGrid;
import ru.viz.clinic.data.EventType;
import ru.viz.clinic.data.OrderState;
import ru.viz.clinic.data.entity.Engineer;
import ru.viz.clinic.data.entity.Order;
import ru.viz.clinic.help.Helper;
import ru.viz.clinic.security.AuthenticationService;
import ru.viz.clinic.service.EngineerService;
import ru.viz.clinic.service.OrderService;
import ru.viz.clinic.service.PersonalService;
import ru.viz.clinic.service.RecordService;
import ru.viz.clinic.views.MainLayout;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static ru.viz.clinic.help.Translator.*;

@PageTitle("Заявки")
@Route(value = "EngineerOrderView", layout = MainLayout.class)
@RolesAllowed("ENGINEER")
@Log4j2
public class EngineerOrderView extends OrderView<EngineerOrderGrid> {
    private Engineer engineer;

    public EngineerOrderView(
            @NotNull final OrderService orderService,
            @NotNull final RecordService recordService,
            @NotNull final EngineerService engineerService
    ) {
        super(Objects.requireNonNull(orderService), Objects.requireNonNull(recordService));
        Objects.requireNonNull(engineerService);
        engineerService.getLoggedEngineer().ifPresent(engineer -> {
            this.engineer = Objects.requireNonNull(engineer);
            createGrid();
        });
    }

    private void createGrid() {
        this.orderGrid = new EngineerOrderGrid(this.engineer, Objects.requireNonNull(recordService));
        this.orderGrid.setItems(orderService.getByHospital(this.engineer.getHospital().getId()));
        this.orderGrid.addListener(EngineerOrderGrid.AdoptEvent.class, this::handleAdoptEvent);
        this.orderGrid.addListener(EngineerOrderGrid.CommentEvent.class, this::handleCommentEvent);
        this.orderGrid.addListener(EngineerOrderGrid.LeaveEvent.class, this::handleLeaveEvent);
        this.add(orderGrid);
    }

    private void handleAdoptEvent(@NotNull final EngineerOrderGrid.AdoptEvent event) {
        Objects.requireNonNull(event);
        final Order order = Objects.requireNonNull(event.getOrder());
        orderService.adoptOrder(order, engineer).ifPresent(savedOrder -> {
            recordService.addRecord(EventType.ADOPT_ORDER, engineer, savedOrder);
            updateGrid();
        });
    }

    private void handleCommentEvent(@NotNull final EngineerOrderGrid.CommentEvent event) {
        Objects.requireNonNull(event);
        final Order order = Objects.requireNonNull(event.getOrder());
        final String comment = Objects.requireNonNull(event.getComment());
        recordService.addRecord(EventType.NOTE, engineer, order, comment);
        updateGrid();
    }

    private void handleLeaveEvent(EngineerOrderGrid.LeaveEvent event) {
        Objects.requireNonNull(event);
        final Order order = Objects.requireNonNull(event.getOrder());
        orderService.leaveOrder(order).ifPresent(savedOrder -> {
            recordService.addRecord(EventType.LIVE_ORDER, engineer, savedOrder, event.getComment());
            updateGrid();
        });
    }

    private void updateGrid() {
        orderGrid.setItems(orderService.getByHospital(engineer.getHospital().getId()));
        orderGrid.getListDataView().refreshAll();
    }
}
