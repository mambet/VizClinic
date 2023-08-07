package ru.viz.clinic.views.order;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.constraints.NotNull;
import lombok.extern.log4j.Log4j2;
import ru.viz.clinic.component.TopicBox;
import ru.viz.clinic.component.dialog.OrderDialog;
import ru.viz.clinic.component.grid.MedicOrderGrid;
import ru.viz.clinic.data.OrderState;
import ru.viz.clinic.data.entity.*;
import ru.viz.clinic.data.EventType;
import ru.viz.clinic.security.AuthenticationService;
import ru.viz.clinic.service.OrderService;
import ru.viz.clinic.service.PersonalService;
import ru.viz.clinic.service.RecordService;
import ru.viz.clinic.views.MainLayout;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static ru.viz.clinic.help.Translator.BTN_CONFIRM_CREATE_PLUS;
import static ru.viz.clinic.help.Translator.DLH_ORDER;

@PageTitle("Заявки")
@Route(value = "MedicOrderView", layout = MainLayout.class)
@RolesAllowed("MEDIC")
@Log4j2
public class MedicOrderView extends OrderView<MedicOrderGrid> {
    private final Medic medic;

    public MedicOrderView(
            @NotNull final OrderService orderService,
            @NotNull final RecordService recordService,
            @NotNull final PersonalService personalService,
            @NotNull final AuthenticationService authenticationService

    ) {
        super(Objects.requireNonNull(orderService),
                Objects.requireNonNull(recordService),
                Objects.requireNonNull(personalService),
                Objects.requireNonNull(authenticationService));

        this.medic = Objects.requireNonNull(getMedic());
        this.orderGrid = new MedicOrderGrid(Objects.requireNonNull(recordService));
        this.orderGrid.setItems(orderService.getByDepartment(medic.getDepartment().getId()));
        this.orderGrid.addListener(MedicOrderGrid.CloseEvent.class, this::handleCloseEvent);
        this.orderGrid.addListener(MedicOrderGrid.EditEvent.class, this::handleEditEvent);
        this.add(new TopicBox(DLH_ORDER, createOrderButton(), orderGrid));
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

        Collection<Engineer> engineers = medic.getDepartment().getHospital().getEngineers();
        Collection<Equipment> equipment = medic.getDepartment().getEquipment();
        // TODO not empty           equipment
        // TODO not empty           engineers
        OrderDialog orderDialog = new OrderDialog(engineers, equipment,
                Order.builder()
                        .medic(Objects.requireNonNull(medic))
                        .department(Objects.requireNonNull(medic.getDepartment()))
                        .records(new ArrayList<>())
                        .build());
        orderDialog.addListener(OrderDialog.UpdateOrder.class, order -> createOrder(order.getOrder()));
        orderDialog.open();
    }

    //event from Grid
    //open Dialog
    private void handleEditEvent(MedicOrderGrid.EditEvent event) {
        log.info("edit order id {}", event.getOrder().getId());
        Objects.requireNonNull(event.getOrder());
        //TODO orElse
        Collection<Engineer> engineers = medic.getDepartment().getHospital().getEngineers(); //reqnon null
        Collection<Equipment> equipment = medic.getDepartment().getEquipment(); //reqnonnull

        OrderDialog orderDialog = new OrderDialog(engineers, equipment, event.getOrder());
        orderDialog.addListener(OrderDialog.UpdateOrder.class, order -> updateOrder(order.getOrder()));
        orderDialog.open();
    }

    private void handleCloseEvent(MedicOrderGrid.CloseEvent event) {
        Order order = Objects.requireNonNull(event.getOrder()); //TODO event reqnotnull
        log.info("close order id {}", order.getId());
        closeOrder(order);
    }

    private void createOrder(@NotNull final Order order) {
        Objects.requireNonNull(order);
        order.setOrderState(OrderState.READY);
        recordService.createRecord(EventType.START_ORDER, medic, saveOrder(order));
        updateGrid();
    }

    private void updateOrder(
            @NotNull final Order order
    ) {
        Objects.requireNonNull(order);
        recordService.createRecord(EventType.UPDATE_ORDER, medic, saveOrder(order));
        updateGrid();
    }

    private void closeOrder(
            @NotNull final Order order
    ) {
        order.setOrderState(OrderState.DONE);
        order.setFinishEngineer(order.getOwnerEngineer());
        order.setOwnerEngineer(null);
        order.setEndTime(LocalDateTime.now());
        recordService.createRecord(EventType.FINISH_ORDER, medic, saveOrder(order));
        updateGrid();
    }

    private void updateGrid() {
        orderGrid.setItems(orderService.getByDepartment(medic.getDepartment().getId()));
        orderGrid.getListDataView().refreshAll();
    }

    protected Medic getMedic() {
        AtomicReference<Medic> atomicReference = new AtomicReference<>();
        authenticationService.getUserDetails().flatMap(personalService::getLoggedPersonal)
                .ifPresent(personal -> {
                    if (personal instanceof Medic e) {
                        atomicReference.set(e);
                    }
                });
        return atomicReference.get();
    }
}
