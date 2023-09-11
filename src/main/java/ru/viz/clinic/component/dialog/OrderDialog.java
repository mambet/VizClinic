package ru.viz.clinic.component.dialog;

import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.value.ValueChangeMode;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import ru.viz.clinic.component.components.EquipmentSelect;
import ru.viz.clinic.converter.EntityToStringConverter;
import ru.viz.clinic.data.entity.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import static ru.viz.clinic.help.Translator.*;

public class OrderDialog extends VizConfirmDialog<Order> {
    final EquipmentSelect equipmentSelect = EquipmentSelect.createDepartmentSelect();
    final MultiSelectComboBox<Engineer> engineerSelect = new MultiSelectComboBox<>();

    private OrderDialog(
            @NotNull final Order order,
            @NotNull final Collection<Engineer> engineers,
            @NotNull final Collection<Equipment> equipments
    ) {
        super(DLH_CREATE_ORDER, Objects.requireNonNull(order));

        Objects.requireNonNull(equipments);
        Objects.requireNonNull(engineers);

        engineerSelect.setLabel(LBL_ENGINEER);
        engineerSelect.setItemLabelGenerator(EntityToStringConverter::convertToPresentation);
        engineerSelect.setItems(engineers);
        equipmentSelect.setItems(equipments);

        final TextArea description = new TextArea(LBL_EQUIPMENT_DESCRIPTION);
        description.setValueChangeMode(ValueChangeMode.EAGER);

        binder.forField(equipmentSelect)
                .asRequired()
                .bind(Order::getEquipment, Order::setEquipment);
        binder.forField(engineerSelect)
                .asRequired()
                .bind(Order::getDestinationEngineers, Order::setDestinationEngineers);
        binder.forField(description).bind(Order::getDescription, Order::setDescription);
        binder.readBean(order);
        this.add(new FormLayout(equipmentSelect, engineerSelect, description));
    }

    public static OrderDialog getCreateDialog(
            @NotNull final Collection<Engineer> engineers,
            @NotNull final Collection<Equipment> equipment,
            @NotNull final Medic medic
    ) {
        final OrderDialog orderDialog = new OrderDialog(Order.builder()
                .medic(Objects.requireNonNull(medic))
                .build(), engineers, equipment);
        orderDialog.initCreate();
        return orderDialog;
    }

    public static OrderDialog getUpdateDialog(
            @NotNull final Order order,
            @NotNull final Collection<Engineer> engineers,
            @NotNull final Collection<Equipment> equipment
    ) {
        final OrderDialog orderDialog = new OrderDialog(order, engineers, equipment);
        orderDialog.initUpdate();
        return orderDialog;
    }

    @Override
    protected void handleCreate() {
        fireEvent(new CreateOrderEvent(this, Objects.requireNonNull(item)));
    }

    @Override
    protected void handleUpdate() {
        fireEvent(new UpdateOrderEvent(this, Objects.requireNonNull(item)));
    }

    @Getter
    public static class CreateOrderEvent extends AbstractDialogEvent<OrderDialog, Order> {
        public CreateOrderEvent(
                @NotNull final OrderDialog source,
                @NotNull final Order order
        ) {
            super(Objects.requireNonNull(source), Objects.requireNonNull(order));
        }
    }

    @Getter
    public static class UpdateOrderEvent extends AbstractDialogEvent<OrderDialog, Order> {
        public UpdateOrderEvent(
                @NotNull final OrderDialog source,
                @NotNull final Order order
        ) {
            super(Objects.requireNonNull(source), Objects.requireNonNull(order));
        }
    }
}
