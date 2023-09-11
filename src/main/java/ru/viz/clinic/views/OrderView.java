package ru.viz.clinic.views;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import ru.viz.clinic.component.dialog.CommentRecordDialog;
import ru.viz.clinic.component.dialog.LeaveRecordDialog;
import ru.viz.clinic.component.dialog.OrderCloseDialog;
import ru.viz.clinic.component.dialog.OrderDialog;
import ru.viz.clinic.component.grid.OrderGrid;
import ru.viz.clinic.data.EngineersAndEquipment;
import ru.viz.clinic.data.EventType;
import ru.viz.clinic.data.entity.Engineer;
import ru.viz.clinic.data.entity.Equipment;
import ru.viz.clinic.data.entity.Order;
import ru.viz.clinic.data.entity.Personal;
import ru.viz.clinic.help.Translator;
import ru.viz.clinic.service.EngineerService;
import ru.viz.clinic.service.EquipmentService;
import ru.viz.clinic.service.OrderService;
import ru.viz.clinic.service.RecordService;

import java.util.*;

import static ru.viz.clinic.help.Helper.showErrorNotification;

@Slf4j
public abstract class OrderView<T extends OrderGrid> extends VerticalLayout {
    protected final OrderService orderService;
    protected final RecordService recordService;
    private final EngineerService engineerService;
    private final EquipmentService equipmentService;
    protected T orderGrid;

    public OrderView(
            @NotNull final OrderService orderService,
            @NotNull final RecordService recordService,
            @NotNull final EngineerService engineerService,
            @NotNull final EquipmentService equipmentService
    ) {
        this.orderService = Objects.requireNonNull(orderService);
        this.recordService = Objects.requireNonNull(recordService);
        this.engineerService = Objects.requireNonNull(engineerService);
        this.equipmentService = Objects.requireNonNull(equipmentService);
    }

    protected static Optional<EngineersAndEquipment> isEngineersAndEquipment(
            @NotNull final Collection<Engineer> engineers,
            @NotNull final Collection<Equipment> equipment
    ) {
        if (engineers.isEmpty()) {
            showErrorNotification(Translator.ERR_MSG_NO_ENGINEERS);
            return Optional.empty();
        } else if (equipment.isEmpty()) {
            showErrorNotification(Translator.ERR_MSG_NO_EQUIPMENT);
            return Optional.empty();
        } else {
            return Optional.of(new EngineersAndEquipment(engineers, equipment));
        }
    }

    private void updateOrder(@NotNull final Order order) {
        Objects.requireNonNull(order);
        orderService.updateOrder(order).ifPresent(savedOrder -> {
            recordService.addRecord(EventType.UPDATE_ORDER, getPersonal(), savedOrder);
            updateGrid();
        });
    }

    private void commentOrder(
            @NotNull final Order order,
            @NotNull final String comment
    ) {
        Objects.requireNonNull(order);
        recordService.addRecord(EventType.NOTE, getPersonal(), order, comment);
        updateGrid();
    }

    private void leaveOrder(
            @NotNull final Order order,
            @NotNull final String comment
    ) {
        Objects.requireNonNull(order);
        orderService.leaveOrder(order).ifPresent(savedOrder ->
        {
            recordService.addRecord(EventType.LIVE_ORDER, getPersonal(), savedOrder, comment);
            updateGrid();
        });
    }

    private void closeOrder(@NotNull final Order order) {
        Objects.requireNonNull(order);
        orderService.closeOrder(order).ifPresent(savedOrder -> {
            recordService.addRecord(EventType.FINISH_ORDER, getPersonal(), savedOrder);
            updateGrid();
        });
    }

    private void deleteOrder(@NotNull final Order order) {
        Objects.requireNonNull(order);
        orderService.delete(order);
        updateGrid();
    }

    private void setActiveOrder(@NotNull final Order order,
                                final boolean active
    ) {
        Objects.requireNonNull(order);
        orderService.setActive(order, active);
        updateGrid();
    }

    protected void handleUpdateEvent(@NotNull final OrderGrid.UpdateGridEvent event) {
        Objects.requireNonNull(event.getEntity());
        getEngineersAndEquipmentByOrder(event.getEntity()).ifPresent(engineersAndEquipment ->
        {
            final OrderDialog orderDialog = OrderDialog.getUpdateDialog(event.getEntity(),
                    engineersAndEquipment.engineers(),
                    engineersAndEquipment.equipment());
            orderDialog.addListener(OrderDialog.UpdateOrderEvent.class, order -> updateOrder(order.getEntity()));
            orderDialog.open();
        });
    }

    protected void handleCommentEvent(@NotNull final OrderGrid.CommentGridEvent event) {
        Objects.requireNonNull(event);
        final Order order = Objects.requireNonNull(event.getEntity());
        final CommentRecordDialog commentRecordDialog = new CommentRecordDialog(order);
        commentRecordDialog.addListener(CommentRecordDialog.CommentOrderEvent.class, e -> {
            commentOrder(order, e.getComment());
        });
        commentRecordDialog.open();
    }

    protected void handleLeaveEvent(@NotNull final OrderGrid.LeaveGridEvent event) {
        Objects.requireNonNull(event);
        final Order order = Objects.requireNonNull(event.getEntity());
        final LeaveRecordDialog leaveRecordDialog = new LeaveRecordDialog(order);
        leaveRecordDialog.addListener(LeaveRecordDialog.LeaveOrderEvent.class, e -> leaveOrder(order, e.getComment()));
        leaveRecordDialog.open();
    }

    protected void handleCloseEvent(@NotNull final OrderGrid.CloseGridEvent event) {
        Objects.requireNonNull(event);
        final OrderCloseDialog orderCloseDialog = new OrderCloseDialog();
        orderCloseDialog.addConfirmListener(confirmEvent -> closeOrder(event.getEntity()));
        orderCloseDialog.open();
    }

    protected void handleSwitchEvent(@NotNull final OrderGrid.SetActiveEvent event) {
        Objects.requireNonNull(event);
        setActiveOrder(event.getEntity(), event.isActive());
    }

    protected void handlerDeleteOrderEvent(@NotNull final OrderGrid.DeleteGridEvent event) {
        Objects.requireNonNull(event);
        deleteOrder(event.getEntity());
    }

    protected void updateGrid() {
        this.orderGrid.setItems(getItems());
    }

    protected abstract List<Order> getItems();

    protected abstract Personal getPersonal();

    protected Optional<EngineersAndEquipment> getEngineersAndEquipmentByOrder(@NotNull final Order order) {
        Objects.requireNonNull(order);
        Collection<Engineer> engineers = new ArrayList<>();
        Collection<Equipment> equipment = new ArrayList<>();
        try {
            final Long hospitalId = order.getEquipment().getDepartment().getHospital().getId();
            final Long departmentId = order.getEquipment().getDepartment().getId();
            engineers = engineerService.getActiveByHospitalId(hospitalId);
            equipment = equipmentService.getActiveByDepartmentId(departmentId);
        } catch (final Exception e) {
            log.error("error: ", e);
        }
        return isEngineersAndEquipment(engineers, equipment);
    }
}