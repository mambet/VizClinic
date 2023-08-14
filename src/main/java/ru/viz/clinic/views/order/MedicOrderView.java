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
import ru.viz.clinic.data.EventType;
import ru.viz.clinic.data.entity.Engineer;
import ru.viz.clinic.data.entity.Equipment;
import ru.viz.clinic.data.entity.Medic;
import ru.viz.clinic.data.entity.Order;
import ru.viz.clinic.help.Translator;
import ru.viz.clinic.service.EngineerService;
import ru.viz.clinic.service.MedicService;
import ru.viz.clinic.service.OrderService;
import ru.viz.clinic.service.RecordService;
import ru.viz.clinic.views.MainLayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import static ru.viz.clinic.help.Helper.showErrorNotification;
import static ru.viz.clinic.help.Translator.BTN_CONFIRM_CREATE_PLUS;

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
        super(Objects.requireNonNull(orderService), Objects.requireNonNull(recordService));
        Objects.requireNonNull(medicService);
        this.engineerService =  Objects.requireNonNull(engineerService);
        Objects.requireNonNull(medicService).getLoggedMedic().ifPresent(medic -> {
            this.medic = Objects.requireNonNull(medic);
            createGrid();
        });
    }

    private void createGrid() {
        this.orderGrid = new MedicOrderGrid(Objects.requireNonNull(recordService));
        this.orderGrid.setItems(orderService.getByDepartment(medic.getDepartment().getId()));
        this.orderGrid.addListener(MedicOrderGrid.CloseGridEvent.class, this::handleCloseEvent);
        this.orderGrid.addListener(MedicOrderGrid.UpdateGridEvent.class, this::handleUpdateEvent);
        this.add(createOrderButton(), orderGrid);
    }

    private Button createOrderButton() {
        final Button button = new Button(BTN_CONFIRM_CREATE_PLUS);
        button.addClickListener(this::handleCreateOrderClick);
        return button;
    }

    //event from button
    //open Dialog
    private void handleCreateOrderClick(final ClickEvent<Button> buttonClickEvent) {
        final Result result = getResult();

        if (!result.engineers.isEmpty() && !result.equipment.isEmpty()) {
            final OrderDialog orderDialog = new OrderDialog(result.engineers, result.equipment,
                    Order.builder()
                            .medic(Objects.requireNonNull(medic))
                            .records(new ArrayList<>())
                            .build());
            orderDialog.addListener(OrderDialog.UpdateOrder.class, order -> createOrder(
                    Objects.requireNonNull(order.getEntity())));
            orderDialog.open();
        } else if (result.engineers.isEmpty()) {
            showErrorNotification(Translator.ERR_MSG_NO_ENGINEERS);
        } else {
            showErrorNotification(Translator.ERR_MSG_NO_EQUIPMENT);
        }
    }

    //event from Grid
    //open Dialog
    private void handleUpdateEvent(final MedicOrderGrid.UpdateGridEvent event) {
        Objects.requireNonNull(event.getEntity());
        final Result result = getResult();

        if (!result.engineers().isEmpty() && !result.equipment().isEmpty()) {
            final OrderDialog orderDialog = new OrderDialog(result.engineers(), result.equipment(), event.getEntity());
            orderDialog.addListener(OrderDialog.UpdateOrder.class, order -> updateOrder(order.getEntity()));
            orderDialog.open();
        } else if (result.engineers().isEmpty()) {
            showErrorNotification(Translator.ERR_MSG_NO_ENGINEERS);
        } else {
            showErrorNotification(Translator.ERR_MSG_NO_EQUIPMENT);
        }
    }

    private void handleCloseEvent(@NotNull final MedicOrderGrid.CloseGridEvent event) {
        Objects.requireNonNull(event);
        final Order order = Objects.requireNonNull(event.getEntity());
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

    private Result getResult() {
        Collection<Engineer> engineers = new ArrayList<>();
        Collection<Equipment> equipment = new ArrayList<>();
        try {
            engineers = engineerService.getByHospitalId(medic.getDepartment().getHospital().getId());
            equipment = medic.getDepartment().getEquipment();
        } catch (final Exception e) {

            log.error("error: ", e);
        }
        return new Result(engineers, equipment);
    }

    private record Result(Collection<Engineer> engineers, Collection<Equipment> equipment) {
    }
}
