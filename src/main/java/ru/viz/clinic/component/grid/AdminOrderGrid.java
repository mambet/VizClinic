package ru.viz.clinic.component.grid;

import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import jakarta.validation.constraints.NotNull;
import ru.viz.clinic.data.OrderState;
import ru.viz.clinic.data.entity.Department;
import ru.viz.clinic.data.entity.Equipment;
import ru.viz.clinic.data.entity.Hospital;
import ru.viz.clinic.data.entity.Order;
import ru.viz.clinic.service.RecordService;

import java.util.*;
import java.util.stream.Collectors;

public class AdminOrderGrid extends OrderGrid {
    private AdminOrderGrid(@NotNull final RecordService recordService) {
        super(Objects.requireNonNull(recordService));
    }

    public static AdminOrderGrid createAdminOrderGrid(@NotNull final RecordService recordService) {
        final AdminOrderGrid adminOrderGrid = new AdminOrderGrid(recordService);
        adminOrderGrid.addHospitalColumn(1);
        adminOrderGrid.addDepartmentColumn(2);
        adminOrderGrid.addEquipmentColumn(3);
        return adminOrderGrid;
    }

    @Override
    protected void updateFilter(
            final @NotNull Collection<Order> items,
            final @NotNull GridListDataView<Order> orderGridListDataView
    ) {
        final Set<Hospital> hospitalSet = items.stream()
                .map(order -> order.getEquipment().getDepartment().getHospital()).collect(Collectors.toSet());
        final Set<Department> departmentSet = items.stream()
                .map(order -> order.getEquipment().getDepartment()).collect(Collectors.toSet());
        final Set<Equipment> equipmentSet = items.stream()
                .map(Order::getEquipment).collect(Collectors.toSet());
        setItemsAtFilter(orderGridListDataView, hospitalSet, departmentSet, equipmentSet);
    }

    @Override
    protected void addCustomButton(
            @NotNull final HorizontalLayout layout,
            @NotNull final Order order
    ) {
        if (order.getOrderState().equals(OrderState.READY)) {
            layout.add(editButton(order));
            layout.add(deleteButton(order));
        }

        if (order.getOrderState().equals(OrderState.WORKING)) {
            layout.add(commentOrderButton(order));
            layout.add(leaveOrderButton(order));
            layout.add(closeOrderButton(order));
        }

        if (order.getOrderState().equals(OrderState.DONE)) {
            layout.add(deleteButton(order));
            layout.add(setActivateButton(order));
        }
    }

    private void setItemsAtFilter(
            @NotNull final GridListDataView<Order> orderGridListDataView,
            @NotNull final Set<Hospital> hospitalSet,
            @NotNull final Set<Department> departmentSet,
            @NotNull final Set<Equipment> equipmentSet
    ) {
        getOrderFilter().setDataView(orderGridListDataView);

        getHospitalSelect().setItems(hospitalSet);

        getHospitalSelect().addValueChangeListener(changeEvent -> {
            final Set<Department> departmentSetByHospital = departmentSet.stream()
                    .filter(department -> department.getHospital().equals(changeEvent.getValue()))
                    .collect(Collectors.toSet());
            getDepartmentSelect().setItems(departmentSetByHospital);
        });

        getDepartmentSelect().addValueChangeListener(changeEvent -> {
            final Set<Equipment> equipmentSetByDepartment = equipmentSet.stream()
                    .filter(equipment -> equipment.getDepartment().equals(changeEvent.getValue()))
                    .collect(Collectors.toSet());
            getEquipmentSelect().setItems(equipmentSetByDepartment);
        });
    }

}