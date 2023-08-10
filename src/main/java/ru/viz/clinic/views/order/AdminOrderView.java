package ru.viz.clinic.views.order;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.constraints.NotNull;
import ru.viz.clinic.component.TopicBox;
import ru.viz.clinic.component.dialog.OrderDialog;
import ru.viz.clinic.component.grid.AdminOrderGrid;
import ru.viz.clinic.component.grid.OrderGrid;
import ru.viz.clinic.data.entity.Engineer;
import ru.viz.clinic.data.entity.Equipment;
import ru.viz.clinic.data.entity.Medic;
import ru.viz.clinic.data.entity.Personal;
import ru.viz.clinic.help.Helper;
import ru.viz.clinic.help.Translator;
import ru.viz.clinic.security.AuthenticationService;
import ru.viz.clinic.service.OrderService;
import ru.viz.clinic.service.PersonalService;
import ru.viz.clinic.service.RecordService;
import ru.viz.clinic.views.MainLayout;

import java.util.Collection;
import java.util.Objects;

import static ru.viz.clinic.help.Translator.*;

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