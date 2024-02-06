package ru.viz.clinic.views.engineer;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import ru.viz.clinic.component.grid.EngineerOrderGrid;
import ru.viz.clinic.component.grid.OrderGrid;
import ru.viz.clinic.data.RecordType;
import ru.viz.clinic.data.entity.Engineer;
import ru.viz.clinic.data.entity.Order;
import ru.viz.clinic.data.entity.Personal;
import ru.viz.clinic.service.EngineerService;
import ru.viz.clinic.service.EquipmentService;
import ru.viz.clinic.service.OrderService;
import ru.viz.clinic.service.RecordService;
import ru.viz.clinic.views.MainLayout;
import ru.viz.clinic.views.OrderView;

import java.util.List;
import java.util.Objects;

@PageTitle("Заявки")
@Route(value = "EngineerOrderView", layout = MainLayout.class)
@RolesAllowed("ENGINEER")
@Slf4j
public class EngineerOrderView extends OrderView<EngineerOrderGrid> {
    private Engineer engineer;

    public EngineerOrderView(
            @NotNull final OrderService orderService,
            @NotNull final RecordService recordService,
            @NotNull final EngineerService engineerService,
            @NotNull final EquipmentService equipmentService
    ) {
        super(Objects.requireNonNull(orderService),
                Objects.requireNonNull(recordService),
                Objects.requireNonNull(engineerService),
                Objects.requireNonNull(equipmentService));
        Objects.requireNonNull(engineerService);
        engineerService.getLoggedPersonal().ifPresent(engineer -> {
            this.engineer = Objects.requireNonNull(engineer);
            this.orderGrid = EngineerOrderGrid.createEngineerOrderGrid(engineer, Objects.requireNonNull(recordService));
            this.orderGrid.addListener(EngineerOrderGrid.AdoptGridEvent.class, this::handleAdoptEvent);
            this.orderGrid.addListener(OrderGrid.CommentGridEvent.class, this::handleCommentEvent);
            this.orderGrid.addListener(OrderGrid.LeaveGridEvent.class, this::handleLeaveEvent);
            this.add(orderGrid);
            updateGrid();
        });
    }

    private void handleAdoptEvent(@NotNull final EngineerOrderGrid.AdoptGridEvent event) {
        Objects.requireNonNull(event);
        final Order order = Objects.requireNonNull(event.getEntity());
        orderService.adoptOrder(order, engineer).ifPresent(savedOrder -> {
            recordService.addRecord(RecordType.ADOPT_ORDER, engineer, savedOrder);
            updateGrid();
        });
    }

    @Override
    protected List<Order> getItems() {
        return orderService.getActiveByHospitalId(engineer.getHospital().getId());
    }

    @Override
    protected Personal getPersonal() {
        return engineer;
    }
}