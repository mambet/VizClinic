package ru.viz.clinic.views.order;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.constraints.NotNull;
import lombok.extern.log4j.Log4j2;
import ru.viz.clinic.component.dialog.CommentRecordDialog;
import ru.viz.clinic.component.dialog.LeaveRecordDialog;
import ru.viz.clinic.component.grid.EngineerOrderGrid;
import ru.viz.clinic.data.EventType;
import ru.viz.clinic.data.entity.Engineer;
import ru.viz.clinic.data.entity.Order;
import ru.viz.clinic.service.EngineerService;
import ru.viz.clinic.service.OrderService;
import ru.viz.clinic.service.RecordService;
import ru.viz.clinic.views.MainLayout;

import java.util.Objects;

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
        this.orderGrid = new EngineerOrderGrid(engineer, Objects.requireNonNull(recordService));
        this.orderGrid.setItems(orderService.getByHospital(engineer.getHospital().getId()));
        this.orderGrid.addListener(EngineerOrderGrid.AdoptGridEvent.class, this::handleAdoptEvent);
        this.orderGrid.addListener(EngineerOrderGrid.CommentGridEvent.class, this::handleCommentEvent);
        this.orderGrid.addListener(EngineerOrderGrid.LeaveGridEvent.class, this::handleLeaveEvent);
        this.add(orderGrid);
    }

    private void handleAdoptEvent(@NotNull final EngineerOrderGrid.AdoptGridEvent event) {
        Objects.requireNonNull(event);
        final Order order = Objects.requireNonNull(event.getEntity());
        orderService.adoptOrder(order, engineer).ifPresent(savedOrder -> {
            recordService.addRecord(EventType.ADOPT_ORDER, engineer, savedOrder);
            updateGrid();
        });
    }

    private void handleCommentEvent(@NotNull final EngineerOrderGrid.CommentGridEvent event) {
        Objects.requireNonNull(event);
        final Order order = Objects.requireNonNull(event.getEntity());
        final CommentRecordDialog commentRecordDialog = new CommentRecordDialog(order);
        commentRecordDialog.addListener(CommentRecordDialog.CommentOrderEvent.class, e -> {
            recordService.addRecord(EventType.NOTE, engineer, order, e.getComment());
            updateGrid();
        });
        commentRecordDialog.open();
    }

    private void handleLeaveEvent(final EngineerOrderGrid.LeaveGridEvent event) {
        Objects.requireNonNull(event);
        final Order order = Objects.requireNonNull(event.getEntity());
        final LeaveRecordDialog leaveRecordDialog = new LeaveRecordDialog(order);
        leaveRecordDialog.addListener(LeaveRecordDialog.LeaveOrderEvent.class, e -> {
            orderService.leaveOrder(order).ifPresent(savedOrder -> recordService.addRecord(EventType.LIVE_ORDER,
                    engineer, savedOrder, e.getComment()));
            updateGrid();
        });
        leaveRecordDialog.open();
    }

    private void updateGrid() {
        orderGrid.setItems(orderService.getByHospital(engineer.getHospital().getId()));
        orderGrid.getListDataView().refreshAll();
    }
}
