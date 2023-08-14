package ru.viz.clinic.views.order;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.constraints.NotNull;
import ru.viz.clinic.component.grid.AdminOrderGrid;
import ru.viz.clinic.service.OrderService;
import ru.viz.clinic.service.RecordService;
import ru.viz.clinic.views.MainLayout;

import java.util.Objects;

@PageTitle("Заявки")
@Route(value = "AdminsOrder", layout = MainLayout.class)
@RolesAllowed({"ADMIN"})
public class AdminOrderView extends OrderView<AdminOrderGrid> {
    public AdminOrderView(
            @NotNull final OrderService orderService,
            @NotNull final RecordService recordService
    ) {
        super(Objects.requireNonNull(orderService), Objects.requireNonNull(recordService));
        this.orderGrid = new AdminOrderGrid(Objects.requireNonNull(recordService));
        this.orderGrid.setItems(orderService.getAll());
        this.orderGrid.getListDataView().refreshAll();
        this.add(orderGrid);
    }
}