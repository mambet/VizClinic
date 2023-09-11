package ru.viz.clinic.views.admin;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.constraints.NotNull;
import org.apache.logging.log4j.util.Strings;
import ru.viz.clinic.component.grid.AdminOrderGrid;
import ru.viz.clinic.component.grid.OrderGrid;
import ru.viz.clinic.data.entity.*;
import ru.viz.clinic.service.*;
import ru.viz.clinic.views.MainLayout;
import ru.viz.clinic.views.OrderView;

import java.util.*;

@PageTitle("Заявки")
@Route(value = "AdminsOrder", layout = MainLayout.class)
@RolesAllowed({"ADMIN"})
public class AdminOrderView extends OrderView<AdminOrderGrid> {
    private Admin admin;
    private boolean activeState;

    public AdminOrderView(
            @NotNull final OrderService orderService,
            @NotNull final AdministratorService administratorService,
            @NotNull final RecordService recordService,
            @NotNull final EngineerService engineerService,
            @NotNull final EquipmentService equipmentService
    ) {
        super(Objects.requireNonNull(orderService),
                Objects.requireNonNull(recordService),
                Objects.requireNonNull(engineerService),
                Objects.requireNonNull(equipmentService));
        Objects.requireNonNull(administratorService).getLoggedPersonal().ifPresent(admin -> {
            this.admin = admin;
            this.orderGrid = AdminOrderGrid.createAdminOrderGrid(Objects.requireNonNull(recordService));
            this.orderGrid.addListener(OrderGrid.UpdateGridEvent.class, this::handleUpdateEvent);
            this.orderGrid.addListener(OrderGrid.CloseGridEvent.class, this::handleCloseEvent);
            this.orderGrid.addListener(OrderGrid.DeleteGridEvent.class, this::handlerDeleteOrderEvent);
            this.orderGrid.addListener(OrderGrid.CommentGridEvent.class, this::handleCommentEvent);
            this.orderGrid.addListener(OrderGrid.LeaveGridEvent.class, this::handleLeaveEvent);
            this.orderGrid.addListener(OrderGrid.SetActiveEvent.class, this::handleSwitchEvent);

            final Select<Boolean> activeStateSelect = new Select<>();
            activeStateSelect.setItems(true, false);
            activeStateSelect.addValueChangeListener(event -> {
                activeState = Boolean.TRUE.equals(event.getValue());
                updateGrid();
            });
            activeStateSelect.setValue(true);
            activeStateSelect.setItemLabelGenerator(aBoolean -> {
                if (Boolean.TRUE.equals(aBoolean)) {
                    return "активные";
                } else if (Boolean.FALSE.equals(aBoolean)) {
                    return " не активные";
                }
                return Strings.EMPTY;
            });

            this.setSizeFull();
            final VerticalLayout mainContainer = new VerticalLayout();
            mainContainer.setPadding(false);
            mainContainer.add(orderGrid);

            final Scroller scroller = new Scroller(mainContainer);
            scroller.setScrollDirection(Scroller.ScrollDirection.VERTICAL);
            scroller.setSizeFull();
            add(new HorizontalLayout(activeStateSelect), scroller);
            updateGrid();
        });
    }

    @Override
    protected List<Order> getItems() {
        if (activeState) {
            return orderService.getAllActive();
        } else {
            return orderService.getAllInactive();
        }
    }

    @Override
    protected Personal getPersonal() {
        return admin;
    }
}