package ru.viz.clinic.views.medic;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import ru.viz.clinic.component.dialog.OrderDialog;
import ru.viz.clinic.component.grid.MedicOrderGrid;
import ru.viz.clinic.component.grid.OrderGrid;
import ru.viz.clinic.data.EngineersAndEquipment;
import ru.viz.clinic.data.EventType;
import ru.viz.clinic.data.entity.*;
import ru.viz.clinic.service.*;
import ru.viz.clinic.views.MainLayout;
import ru.viz.clinic.views.OrderView;

import java.util.*;

import static ru.viz.clinic.help.Translator.BTN_CONFIRM_CREATE_PLUS;

@PageTitle("Заявки")
@Route(value = "MedicOrderView", layout = MainLayout.class)
@RolesAllowed("MEDIC")
@Slf4j
public class MedicOrderView extends OrderView<MedicOrderGrid> {
    private final EngineerService engineerService;
    private final EquipmentService equipmentService;
    private Medic medic;

    public MedicOrderView(
            @NotNull final OrderService orderService,
            @NotNull final RecordService recordService,
            @NotNull final MedicService medicService,
            @NotNull final EngineerService engineerService,
            @NotNull final EquipmentService equipmentService

    ) {
        super(Objects.requireNonNull(orderService),
                Objects.requireNonNull(recordService),
                Objects.requireNonNull(engineerService),
                Objects.requireNonNull(equipmentService));
        this.engineerService = Objects.requireNonNull(engineerService);
        this.equipmentService = Objects.requireNonNull(equipmentService);
        Objects.requireNonNull(medicService).getLoggedPersonal().ifPresent(medic -> {
            this.medic = Objects.requireNonNull(medic);
            this.orderGrid = MedicOrderGrid.createMedicOrderGrid(Objects.requireNonNull(recordService));
            this.orderGrid.addListener(OrderGrid.UpdateGridEvent.class, this::handleUpdateEvent);
            this.orderGrid.addListener(OrderGrid.CloseGridEvent.class, this::handleCloseEvent);
            this.orderGrid.addListener(OrderGrid.DeleteGridEvent.class, this::handlerDeleteOrderEvent);
            this.add(createOrderButton(), orderGrid);
            updateGrid();
        });
    }

    private void handleCreateOrderClick(final ClickEvent<Button> buttonClickEvent) {
        getEngineersAndEquipmentByMedic().ifPresent(engineersAndEquipment ->
        {
            final OrderDialog orderDialog = OrderDialog.getCreateDialog(engineersAndEquipment.engineers(),
                    engineersAndEquipment.equipment(), medic);
            orderDialog.addListener(OrderDialog.CreateOrderEvent.class, order -> createOrder(
                    Objects.requireNonNull(order.getEntity())));
            orderDialog.open();
        });
    }

    private Button createOrderButton() {
        final Button button = new Button(BTN_CONFIRM_CREATE_PLUS);
        button.addClickListener(this::handleCreateOrderClick);
        return button;
    }

    private void createOrder(@NotNull final Order order) {
        Objects.requireNonNull(order);
        orderService.createOrder(order).ifPresent(savedOrder -> {
            recordService.addRecord(EventType.START_ORDER, medic, savedOrder);
            updateGrid();
        });
    }

    protected Optional<EngineersAndEquipment> getEngineersAndEquipmentByMedic() {
        Collection<Engineer> engineers = new ArrayList<>();
        Collection<Equipment> equipment = new ArrayList<>();
        try {
            final Long hospitalId = medic.getDepartment().getHospital().getId();
            final Long departmentId = medic.getDepartment().getId();
            engineers = engineerService.getActiveByHospitalId(hospitalId);
            equipment = equipmentService.getActiveByDepartmentId(departmentId);
        } catch (final Exception e) {
            log.error("error: ", e);
        }
        return isEngineersAndEquipment(engineers, equipment);
    }

    @Override
    protected List<Order> getItems() {
        return orderService.getActiveByDepartmentId(medic.getDepartment().getId());
    }

    @Override
    protected Personal getPersonal() {
        return medic;
    }
}