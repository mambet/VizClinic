package ru.viz.clinic.component.grid;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import ru.viz.clinic.data.OrderState;
import ru.viz.clinic.data.entity.*;
import ru.viz.clinic.service.RecordService;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.viz.clinic.help.Translator.*;

public class EngineerOrderGrid extends OrderGrid {
    private final Engineer engineer;

    private EngineerOrderGrid(
            @NotNull final Engineer engineer,
            @NotNull final RecordService recordService
    ) {
        super(Objects.requireNonNull(recordService));
        this.engineer = Objects.requireNonNull(engineer);
    }

    public static EngineerOrderGrid createEngineerOrderGrid(
            @NotNull final Engineer engineer,
            @NotNull final RecordService recordService
    ) {
        final EngineerOrderGrid engineerOrderGrid = new EngineerOrderGrid(engineer, recordService);
        engineerOrderGrid.addDepartmentColumn(1);
        engineerOrderGrid.addEquipmentColumn(2);
        return engineerOrderGrid;
    }

    private void setItemsAtFilter(
            @NotNull final GridListDataView<Order> orderGridListDataView,
            @NotNull final Set<Department> departmentSet,
            @NotNull final Set<Equipment> equipmentSet
    ) {
        getOrderFilter().setDataView(orderGridListDataView);
        getDepartmentSelect().setItems(departmentSet);

        getDepartmentSelect().addValueChangeListener(changeEvent -> {
            final Set<Equipment> equipmentSetByDepartment = equipmentSet.stream()
                    .filter(equipment -> equipment.getDepartment().equals(changeEvent.getValue()))
                    .collect(Collectors.toSet());
            getEquipmentSelect().setItems(equipmentSetByDepartment);
        });
    }

    @Override
    protected void updateFilter(
            @NotNull final Collection<Order> items,
            @NotNull final GridListDataView<Order> orderGridListDataView
    ) {
        final Set<Department> departmentSet = items.stream()
                .map(order -> order.getEquipment().getDepartment()).collect(Collectors.toSet());
        final Set<Equipment> equipmentSet = items.stream()
                .map(Order::getEquipment).collect(Collectors.toSet());

        setItemsAtFilter(orderGridListDataView, departmentSet, equipmentSet);
    }

    @Override
    protected void addCustomButton(
            @NotNull final HorizontalLayout layout,
            @NotNull final Order order
    ) {
        if (order.getOrderState().equals(OrderState.READY)
                && (order.getDestinationEngineers().isEmpty() || order.getDestinationEngineers().contains(engineer))) {
            layout.add(adoptOrderButton(order));
        }
        if (order.getOwnerEngineer() != null && order.getOwnerEngineer().equals(engineer)
                && order.getOrderState().equals(OrderState.WORKING)) {
            layout.add(commentOrderButton(order), leaveOrderButton(order));
        }
    }
}