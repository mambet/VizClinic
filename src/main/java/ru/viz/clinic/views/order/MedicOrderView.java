package ru.viz.clinic.views.order;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.constraints.NotNull;
import lombok.extern.log4j.Log4j2;
import ru.viz.clinic.component.dialog.OrderDialog;
import ru.viz.clinic.component.grid.MedicOrderGrid;
import ru.viz.clinic.data.entity.*;
import ru.viz.clinic.data.EventType;
import ru.viz.clinic.service.*;
import ru.viz.clinic.views.MainLayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import static ru.viz.clinic.help.Translator.*;

@PageTitle("Заявки")
@Route(value = "MedicOrderView", layout = MainLayout.class)
@RolesAllowed("MEDIC")
@Log4j2
public class MedicOrderView extends OrderView<MedicOrderGrid> {
    private Medic medic;
    private final EngineerService engineerService;

    public MedicOrderView(
            @NotNull final OrderService orderService,
            @NotNull final RecordService recordService,
            @NotNull final MedicService medicService,
            @NotNull final EngineerService engineerService

    ) {
        super(Objects.requireNonNull(orderService),
                Objects.requireNonNull(recordService));

        this.engineerService = Objects.requireNonNull(engineerService);

        Objects.requireNonNull(medicService).getLoggedMedic().ifPresent(medic -> {
            this.medic = Objects.requireNonNull(medic);
            createGrid();
        });
    }

    private void createGrid() {
        this.orderGrid = new MedicOrderGrid(Objects.requireNonNull(recordService));
        this.orderGrid.setItems(orderService.getByDepartment(this.medic.getDepartment().getId()));
        this.orderGrid.addListener(MedicOrderGrid.CloseEvent.class, this::handleCloseEvent);
        this.orderGrid.addListener(MedicOrderGrid.UpdateEvent.class, this::handleUpdateEvent);
        this.add(createOrderButton(), orderGrid);
    }

    private Button createOrderButton() {
        Button button = new Button(BTN_CONFIRM_CREATE_PLUS);
        button.addClickListener(this::handleCreateOrderClick);
        return button;
    }

    //event from button
    //open Dialog
    private void handleCreateOrderClick(ClickEvent<Button> buttonClickEvent) {
        log.info("create order");

        Collection<Engineer> engineers = engineerService.getByHospitalId(medic.getDepartment().getHospital().getId());
        Collection<Equipment> equipment = medic.getDepartment().getEquipment();
        // TODO not empty           equipment
        // TODO not empty           engineers
        OrderDialog orderDialog = new OrderDialog(engineers, equipment,
                Order.builder()
                        .medic(Objects.requireNonNull(medic))
                        .records(new ArrayList<>())
                        .build());
        orderDialog.addListener(OrderDialog.UpdateOrder.class, order -> createOrder(
                Objects.requireNonNull(order.getOrder())));
        orderDialog.open();
    }

    //event from Grid
    //open Dialog
    private void handleUpdateEvent(MedicOrderGrid.UpdateEvent event) {
        log.info("edit order id {}", event.getOrder().getId());
        Objects.requireNonNull(event.getOrder());
        //TODO orElse
        Collection<Engineer> engineers = engineerService.getByHospitalId(medic.getDepartment().getHospital().getId());
        Collection<Equipment> equipment = medic.getDepartment().getEquipment(); //reqnonnull

        OrderDialog orderDialog = new OrderDialog(engineers, equipment, event.getOrder());
        orderDialog.addListener(OrderDialog.UpdateOrder.class, order -> updateOrder(order.getOrder()));
        orderDialog.open();
    }

    private void handleCloseEvent(@NotNull final MedicOrderGrid.CloseEvent event) {
        Objects.requireNonNull(event);
        Order order = Objects.requireNonNull(event.getOrder());
        log.info("close order with id {}", order.getId());
        closeOrder(order);
    }

    private void createOrder(@NotNull final Order order) {
        Objects.requireNonNull(order);
        orderService.createOrder(order).ifPresent(savedOrder -> {
            recordService.addRecord(EventType.START_ORDER, medic, savedOrder);
            updateGrid();
        });
    }

    private void updateOrder(@NotNull final Order order) {
        Objects.requireNonNull(order);
        orderService.updateOrder(order).ifPresent(savedOrder -> {
            recordService.addRecord(EventType.UPDATE_ORDER, medic, savedOrder);
            updateGrid();
        });
    }

    private void closeOrder(@NotNull final Order order) {
        orderService.closeOrder(order).ifPresent(savedOrder -> {
            recordService.addRecord(EventType.FINISH_ORDER, medic, savedOrder);
            updateGrid();
        });
    }

    private void updateGrid() {
        orderGrid.setItems(orderService.getByDepartment(medic.getDepartment().getId()));
        orderGrid.getListDataView().refreshAll();
    }
}
