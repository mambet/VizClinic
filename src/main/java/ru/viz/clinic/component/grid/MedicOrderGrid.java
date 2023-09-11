package ru.viz.clinic.component.grid;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import jakarta.validation.constraints.NotNull;
import ru.viz.clinic.component.dialog.OrderCloseDialog;
import ru.viz.clinic.data.OrderState;
import ru.viz.clinic.data.entity.Department;
import ru.viz.clinic.data.entity.Equipment;
import ru.viz.clinic.data.entity.Hospital;
import ru.viz.clinic.data.entity.Order;
import ru.viz.clinic.service.RecordService;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.viz.clinic.help.Translator.TTP_CLOSE_ORDER;

public class MedicOrderGrid extends OrderGrid {
    private MedicOrderGrid(@NotNull final RecordService recordService) {
        super(Objects.requireNonNull(recordService));
    }

    public static MedicOrderGrid createMedicOrderGrid(@NotNull final RecordService recordService) {
        final MedicOrderGrid medicOrderGrid = new MedicOrderGrid(recordService);
        medicOrderGrid.addEquipmentColumn(1);
        return medicOrderGrid;
    }

    private void setItemsAtFilter(
            @NotNull final GridListDataView<Order> orderGridListDataView,
            @NotNull final Set<Equipment> equipmentSet
    ) {
        getOrderFilter().setDataView(orderGridListDataView);
        getEquipmentSelect().setItems(equipmentSet);
    }

    @Override
    protected void updateFilter(
            final @NotNull Collection<Order> items,
            final @NotNull GridListDataView<Order> orderGridListDataView
    ) {
        final Set<Equipment> equipmentSet = items.stream()
                .map(Order::getEquipment).collect(Collectors.toSet());
        setItemsAtFilter(orderGridListDataView, equipmentSet);
    }

    @Override
    protected void addCustomButton(
            @NotNull final HorizontalLayout layout,
            @NotNull final Order order
    ) {
        if (order.getOrderState().equals(OrderState.READY)) {
            layout.add(editButton(order), deleteButton(order));
        }
        if (order.getOrderState().equals(OrderState.WORKING)) {
            layout.add(closeOrderButton(order));
        }
    }
}